package com.rishabh.payment.service;

import com.rishabh.payment.dto.OrderEvent;
import com.rishabh.payment.model.Payment;
import com.rishabh.payment.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String,OrderEvent> kafkaTemplate;

    public Mono<Payment> makePayment(Payment payment) {
        // Simulate payment processing logic
        if (payment.getAmount() > 0) {
            payment.setStatus("SUCCESS");
        } else {
            payment.setStatus("FAILED");
        }
        return paymentRepository.save(payment);
    }


    public Mono<Payment> processPayment(OrderEvent orderEvent) {
        // Simulated payment logic
        boolean isPaid = orderEvent.getTotalPrice() < 1000; // mock condition

        Payment payment = Payment.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .amount(orderEvent.getTotalPrice())
                .status(isPaid ? "PAYMENT_COMPLETED" : "PAYMENT_FAILED")
                .build();
        return paymentRepository.save(payment)
                .doOnSuccess(p -> log.info("Payment processed for order {}: {}", orderEvent.getOrderId(), p.getStatus()));
    }
    public Flux<Payment> getPaymentsByUser(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    @KafkaListener(topics = "inventory-status", groupId = "payment-group")
    public void handleInventoryStatus(OrderEvent event) {
        if ("INVENTORY_DEDUCTED".equals(event.getStatus())) {
            // Simulate payment
            log.info("Processing payment for order {}", event.getOrderId());

            OrderEvent paymentEvent = OrderEvent.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .totalPrice(event.getTotalPrice())
                    .status("PAYMENT_COMPLETED")
                    .eventTime(LocalDateTime.now())
                    .build();

            kafkaTemplate.send("payment-status", paymentEvent);
        } else {
            log.warn("Inventory deduction failed for order {}", event.getOrderId());
        }
    }

}

