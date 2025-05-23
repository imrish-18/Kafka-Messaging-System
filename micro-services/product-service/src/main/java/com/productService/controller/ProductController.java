package com.productService.controller;

import com.productService.model.Product;
import com.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getUser(@PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }


    @PostMapping("/SaveProducts")
    public Mono<ResponseEntity<Product>> createUser(@RequestBody Product user) {
        return productService.saveProduct(user)
                .map(ResponseEntity::ok);
    }
    @GetMapping("/GetAllProducts")
    public Flux<Product> getAllUsers() {
        return productService.getAllUsers();
    }
}

