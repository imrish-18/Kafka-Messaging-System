package com.rishabh.payment.repo;

import com.rishabh.payment.model.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {
    Flux<Payment> findByUserId(String userId);
    Mono<Payment> findByOrderId(String orderId);
}

