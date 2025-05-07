package com.rishabh.order_service.controller;

import com.rishabh.order_service.dto.OrderRequestDTO;
import com.rishabh.order_service.model.Order;
import com.rishabh.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public Mono<ResponseEntity<Order>> placeOrder(@RequestBody OrderRequestDTO dto) {
        return orderService.placeOrder(dto)
                .map(ResponseEntity::ok);
    }
}

