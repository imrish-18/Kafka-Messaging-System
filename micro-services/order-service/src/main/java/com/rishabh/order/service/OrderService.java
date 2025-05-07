package com.rishabh.order.service;

import com.rishabh.order.config.OrderKafkaProducer;
import com.rishabh.order.dto.*;
import com.rishabh.order.model.Order;
import com.rishabh.order.repo.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderKafkaProducer kafkaProducer;
    @Autowired
    private WebClient.Builder webClient;


    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackUserService")
    public Mono<User> fetchUser(String userId) {
        return webClient.build()
                .get()
                .uri("http://USER-SERVICE/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class)
                .doOnError(error -> log.error("Error fetching user: {}", error.getMessage()));
    }

    public Mono<User> fallbackUserService(String userId, Throwable t) {
        log.warn("Fallback for userId {} due to {}", userId, t.getMessage());
        return Mono.just(new User("0", "Fallback User", "fallback@example.com"));
    }
    public Mono<Product> fallbackProductService(String productId, Throwable t) {
        log.warn("Fallback for productId {} due to {}", productId, t.getMessage());
        return Mono.just(new Product("0", "Fallback Product", 0.0));
    }
    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "fallbackProductService")
    public Mono<Product> fetchProduct(String productId) {
        return webClient.build()
                .get()
                .uri("http://PRODUCT-SERVICE/product/{id}", productId)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(error -> log.error("Error fetching product: {}", error.getMessage()));
    }


    public Mono<Order> placeOrder(Order order) {
//680618eeb3d98f4a8f24a406 user id
        Mono<User> userMono=fetchUser(order.getUserId());

        log.info("user is fetched with user id {} ",order.getUserId());
        Mono<Product> productMono =fetchProduct(order.getProductId());
       log.info("product is fetched with product id {} ",order.getProductId());
        return Mono.zip(userMono,productMono)
                        .flatMap(tuple->{
                            //✅ Benefits
                            //Parallel fetching of dependent services
                            //
                            //Clean, readable async orchestration
                            //
                            //Ensures all Monos complete before proceeding
                            User user=tuple.getT1();
                            Product product =tuple.getT2();
                            order.setName(user.getUser_name());
                            order.setEmail(user.getUser_email());
                            order.setProductName(product.getProduct_name());
                            log.info("product is placed with product price {} ",product.getProduct_price());
                            order.setTotalPrice(product.getProduct_price()*order.getQuantity());
                            log.info("product is placed with product name {} ",product.getProduct_name());
                            order.setOrderDate(LocalDateTime.now());
                            log.info("product is placed with user {} ",user.getUser_name());
                            return orderRepository.save(order)
                                    .doOnSuccess(kafkaProducer::sendOrderEvent);
                        });
    }

    public Flux<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Flux<Order> getAllOrders() {
        return fetchUser("680618eeb3d98f4a8f24a406")
                .doOnError(t -> log.warn("Error fetching user: {}", t.getMessage()))
                .onErrorResume(t -> {
                    log.warn("Fallback triggered inside getAllOrders: {}", t.getMessage());
                    return fallbackUserService("680618eeb3d98f4a8f24a406", t);
                })
                .doOnNext(user -> log.info("Fetched user (or fallback): {}", user.getUser_name()))
                .thenMany(orderRepository.findAll());
    }

//    public Mono<List<Order>> getAllOrders() {
//        return Mono.zip(
//                fetchUser("680618eeb3d98f4a8f24a406"),
//                orderRepository.findAll().collectList()
//        ).map(tuple -> {
//            User user = tuple.getT1(); // fallback-safe
//            List<Order> orders = tuple.getT2();
//            log.info("Returning {} orders for user {}", orders.size(), user.getName());
//            return orders;
//        });
//    }


    @CircuitBreaker(name = "inventoryServiceCB", fallbackMethod = "fallbackInventory")
    public Mono<String> deductInventory(InventoryRequest request) {
        return webClient.build().post()
                .uri("http://INVENTORY-SERVICE/inventory/deduct")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

    @CircuitBreaker(name = "paymentServiceCB", fallbackMethod = "fallbackPayment")
    public Mono<String> processPayment(PaymentRequest request) {
        return webClient.build().post()
                .uri("http://PAYMENT-SERVICE/payments/process")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }
    public Mono<Order> placeOrderSaga(Order order) {
        String orderId = UUID.randomUUID().toString();
        System.out.println("this is order id ...." + orderId);
        order.setId(orderId);

        InventoryRequest inventoryRequest = new InventoryRequest(order.getProductId(), order.getQuantity());

        // First, fetch user and product in parallel, with fallback to handle failures
        Mono<User> userMono = fetchUser(order.getUserId())
                .onErrorResume(e -> {
                    log.error("Failed to fetch user", e);
                    return Mono.just(new User("0","default-user", "default-email"));
                });
        Mono<Product> productMono = fetchProduct(order.getProductId())
                .onErrorResume(e -> {
                    log.error("Failed to fetch product", e);
                    return Mono.just(new Product("0","default-product", 0.0));
                });

        return Mono.zip(userMono, productMono)
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    Product product = tuple.getT2();

                    // Enrich the order with data
                    order.setName(user.getUser_name());
                    order.setEmail(user.getUser_email());
                    order.setProductName(product.getProduct_name());
                    order.setTotalPrice(product.getProduct_price() * order.getQuantity());
                    order.setOrderDate(LocalDateTime.now());
                    log.info("Order enriched with data. Order ID: {}", orderId);

                    // Log order details before saving
                    log.info("Prepared Order for saving: {}", order);

                    // Prepare payment request
                    PaymentRequest paymentRequest = new PaymentRequest(orderId, order.getTotalPrice(), order.getUserId());

                    // Saga steps: deduct inventory, process payment, then save order
                    return deductInventory(inventoryRequest)
                            .then(processPayment(paymentRequest))
                            .flatMap(__ -> {
                                log.info("Attempting to save order with ID: {}", order.getId());
                                return orderRepository.save(order)
                                        .doOnSuccess(savedOrder -> {
                                            if (savedOrder != null) {
                                                log.info("Order successfully saved with ID: {}", savedOrder.getId());
                                            } else {
                                                log.error("Saved order is null. There was an issue saving the order.");
                                            }
                                        })
                                        .doOnError(err -> log.error("Error while saving the order: {}", err.getMessage()));
                            });
                })
                .doOnSuccess(o -> {
                    if (o != null) {
                        log.info("Order placed with Saga: {}", o);
                    } else {
                        log.warn("Order placed with Saga but received null order.");
                    }
                })
                .onErrorResume(error -> {
                    log.error("Saga failed: {}", error.getMessage());
                    PaymentRequest paymentRequest = new PaymentRequest(order.getId(), order.getTotalPrice(), order.getUserId());
                    return compensateSaga(inventoryRequest, paymentRequest)
                            .then(Mono.error(new RuntimeException("Saga rolled back due to failure.")));
                });
    }




    public Mono<String> fallbackInventory(InventoryRequest request, Throwable t) {
        log.warn("Fallback Inventory: {}", t.getMessage());
        return Mono.error(new RuntimeException("Inventory service unavailable"));
    }

    public Mono<String> fallbackPayment(PaymentRequest request, Throwable t) {
        log.warn("Fallback Payment: {}", t.getMessage());
        return Mono.error(new RuntimeException("Payment service unavailable"));
    }
    private Mono<Void> compensateSaga(InventoryRequest inventoryRequest, PaymentRequest paymentRequest) {
        log.warn("Compensating saga...");

        Mono<Void> refundPayment = webClient.build().post()
                .uri("http://PAYMENT-SERVICE/payments/refund")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Refund failed: {}", e.getMessage());
                    return Mono.empty();
                });

        Mono<Void> restock = webClient.build().post()
                .uri("http://INVENTORY-SERVICE/inventory/restock")
                .bodyValue(inventoryRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Restock failed: {}", e.getMessage());
                    return Mono.empty();
                });

        return Mono.when(refundPayment, restock);
    }

    public Mono<Order> placeOrderSagaKafka(Order order) {
        String orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        order.setOrderDate(LocalDateTime.now());

        return Mono.zip(fetchUser(order.getUserId()), fetchProduct(order.getProductId()))
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    Product product = tuple.getT2();

                    order.setName(user.getUser_name());
                    order.setEmail(user.getUser_email());
                    order.setProductName(product.getProduct_name());
                    order.setTotalPrice(product.getProduct_price() * order.getQuantity());

                    OrderEvent event = OrderEvent.builder()
                            .orderId(orderId)
                            .userId(order.getUserId())
                            .totalPrice(order.getTotalPrice())
                            .status("ORDER_CREATED")
                            .eventTime(LocalDateTime.now())
                            .build();

                    kafkaProducer.sendOrderSagaEvent(event); // send to Kafka topic

                    return Mono.just(order);
                });
    }

    @KafkaListener(topics = "payment-status", groupId = "order-group")
    public void handlePaymentStatus(OrderEvent event) {
        if ("PAYMENT_COMPLETED".equals(event.getStatus())) {
            Order order = Order.builder()
                    .id(event.getOrderId())
                    .userId(event.getUserId())
                    .totalPrice(event.getTotalPrice())
                    .orderDate(event.getEventTime())
                   // .status("COMPLETED")
                    .build();

            orderRepository.save(order).subscribe(o ->
                    log.info("Order completed and saved: {}", o)
            );
        } else {
            log.warn("Payment failed for order {}", event.getOrderId());
            // You may also trigger compensation from here
        }
    }

}

//Mono.zip(...) is used to combine multiple Mono values into a single Mono. It's super helpful when you want to execute multiple async operations in parallel and then do something after all of them complete.