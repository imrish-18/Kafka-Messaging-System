package com.api.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/users")
    public Mono<String> userFallback() {
        log.info("User Service is currently unavailable. Please try again later.");
        return Mono.just("User Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/product")
    public Mono<String> productFallback() {
        log.info("Product Service is currently unavailable. Please try again later.");
        return Mono.just("Product Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/orders")
    public Mono<String> orderFallback() {
        log.info("order Service is currently unavailable. Please try again later.");
        return Mono.just("Order Service is currently unavailable. Please try again later.");
    }
}

