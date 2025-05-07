package com.rishabh.order_service.service;

import com.rishabh.order_service.dto.PaymentRequest;
import com.rishabh.order_service.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentListener {

    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-events", groupId = "payment")
    public void processPayment(PaymentRequest request) {
        boolean paymentSuccess = true; // mock
        kafkaTemplate.send("payment-responses", new PaymentResponse(request.getOrderId(), request.getUserId(), paymentSuccess, "prod-1", 2));
    }
}

