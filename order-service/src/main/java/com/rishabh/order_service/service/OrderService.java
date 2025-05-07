package com.rishabh.order_service.service;

import com.rishabh.order_service.dto.OrderRequestDTO;
import com.rishabh.order_service.dto.OrderStatus;
import com.rishabh.order_service.dto.PaymentRequest;
import com.rishabh.order_service.dto.User;
import com.rishabh.order_service.model.Order;
import com.rishabh.order_service.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  OrderKafkaProducer kafkaProducer;
    @Autowired
    private  WebClient.Builder webClient;


    public Mono<Order> placeOrder(OrderRequestDTO dto) {
        Order order = new Order(UUID.randomUUID().toString(), dto.getUserId(), dto.getProductId(), dto.getQuantity(), OrderStatus.CREATED);
        return orderRepository.save(order)
                .doOnSuccess(o -> kafkaProducer.sendOrderEvent("payment-events", new PaymentRequest(o.getId(), dto.getUserId(), calculateAmount(dto))));
    }

    private Double calculateAmount(OrderRequestDTO dto) {
        return 100.0 * dto.getQuantity(); // mock
    }

    public Mono<Void> updateStatus(String orderId, OrderStatus status) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                }).then();
    }
}













//    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackUserService")
//    public Mono<User> fetchUser(String userId) {
//        return webClient.build()
//                .get()
//                .uri("http://USER-SERVICE/api/users/{id}", userId)
//                .retrieve()
//                .bodyToMono(User.class)
//                .doOnError(error -> log.error("Error fetching user: {}", error.getMessage()));
//    }
//
//    public Mono<Order> placeOrder(Order order) {
//        return fetchUser(order.getUserId())
//                .flatMap(user -> {
//                    order.setStatus(OrderStatus.CREATED);
//                    order.setOrderDate(Instant.now());
//                    return orderRepository.save(order)
//                            .doOnSuccess(kafkaProducer::sendOrderEvent);
//                });
//    }
//
//    // fallback
//    public Mono<User> fallbackUserService(String userId, Throwable ex) {
//        log.warn("Fallback triggered for userId={}, reason={}", userId, ex.getMessage());
//        return Mono.error(new RuntimeException("User Service unavailable"));
//    }