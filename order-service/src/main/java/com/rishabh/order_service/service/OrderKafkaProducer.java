package com.rishabh.order_service.service;

import com.rishabh.order_service.dto.OrderEvent;
import com.rishabh.order_service.dto.PaymentRequest;
import com.rishabh.order_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
public class OrderKafkaProducer {

    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;


    public void sendOrderEvent(String topic , PaymentRequest paymentRequest) {


        kafkaTemplate.send(topic, paymentRequest);
    }
}

