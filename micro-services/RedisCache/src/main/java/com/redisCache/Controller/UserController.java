package com.redisCache.Controller;

import com.redisCache.Service.UserService;
import com.redisCache.model.User;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private  MeterRegistry registry;
    @Autowired
    private Tracer tracer;

    @GetMapping("/traced")
    public String tracedEndpoint() {
        var span = tracer.nextSpan().name("custom-span").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            // Simulate logic
            log.info("Inside custom span logic");
            Thread.sleep(100);
            return "Traced!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error";
        } finally {
            span.end();
        }
    }
    @GetMapping("/custom-metric")
    public String triggerCustomMetric() {
        registry.counter("my_custom_counter").increment();
        return "Custom metric increased";
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }


    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userService.saveUser(user)
                .map(ResponseEntity::ok);
    }
    @GetMapping("/GetAllUsers")
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }


}


