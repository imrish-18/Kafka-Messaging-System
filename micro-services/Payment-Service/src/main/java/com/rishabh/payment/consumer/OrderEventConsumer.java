package com.rishabh.payment.consumer;


import com.rishabh.payment.dto.OrderEvent;
import com.rishabh.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "payment-service")
    public void consume(String message) {
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
            log.info("Received order event: {}", event);
            paymentService.processPayment(event).subscribe();
        } catch (Exception e) {
            log.error("Failed to process event", e);
        }
    }
}

