package com.rishabh.order.config;

import com.rishabh.order.dto.OrderEvent;
import com.rishabh.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.topic.order-events}")
    private String orderEventsTopic;

    public void sendOrderEvent(Order order) {
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .eventTime(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .build();

        log.info("order has been placed ...{} ",order);
        kafkaTemplate.send(orderEventsTopic, event);
    }

    public void sendOrderSagaEvent(OrderEvent event){
        log.info("order placed ... for the event {} ",event);
        kafkaTemplate.send(orderEventsTopic, event);
    }
}

