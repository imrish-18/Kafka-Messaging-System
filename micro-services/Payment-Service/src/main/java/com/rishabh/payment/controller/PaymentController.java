package com.rishabh.payment.controller;

import com.rishabh.payment.model.Payment;
import com.rishabh.payment.model.PaymentRequest;
import com.rishabh.payment.repo.PaymentRepository;
import com.rishabh.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/process")
    public Mono<String> process(@RequestBody PaymentRequest request) {
        if (Math.random() < 0.2) { // 20% failure rate
            return Mono.error(new RuntimeException("Simulated payment failure"));
        }

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setStatus("SUCCESS");

        return paymentRepository.save(payment)
                .thenReturn("Payment processed");
    }


    @PostMapping("/refund")
    public Mono<String> refund(@RequestBody PaymentRequest request) {
        return paymentRepository.findByOrderId(request.getOrderId())
                .flatMap(payment -> paymentRepository.delete(payment).thenReturn("Payment refunded"))
                .switchIfEmpty(Mono.just("No payment found to refund"));
    }
    @PostMapping
    public Mono<Payment> processPayment(@RequestBody Payment payment) {
        return paymentService.makePayment(payment);
    }

    @GetMapping("/user/{userId}")
    public Flux<Payment> getPayments(@PathVariable String userId) {
        return paymentService.getPaymentsByUser(userId);
    }
}

