package com.rishabh.order.controller;

import com.rishabh.order.model.Order;
import com.rishabh.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping("/placeOrder")
    public Mono<ResponseEntity<Order>> placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/user/{userId}")
    public Flux<Order> getOrdersByUser(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/place-saga")
    public Mono<Order> placeOrderWithSaga(@RequestBody Order order) {
        return orderService.placeOrderSagaKafka(order);
    }

}


