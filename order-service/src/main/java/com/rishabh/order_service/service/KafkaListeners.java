package com.rishabh.order_service.service;

import com.rishabh.order_service.dto.InventoryRequest;
import com.rishabh.order_service.dto.InventoryResponse;
import com.rishabh.order_service.dto.PaymentResponse;
import com.rishabh.order_service.dto.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    @Autowired
    private  OrderService orderService;

    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-responses", groupId = "order")
    public void listenPayment(PaymentResponse response) {
        if (response.isSuccess()) {
            orderService.updateStatus(response.getOrderId(), OrderStatus.COMPLETED).subscribe();
            kafkaTemplate.send("inventory-events", new InventoryRequest(response.getOrderId(), response.getProductId(), response.getQuantity()));
        } else {
            orderService.updateStatus(response.getOrderId(), OrderStatus.FAILED).subscribe();
        }
    }

    @KafkaListener(topics = "inventory-responses", groupId = "order")
    public void listenInventory(InventoryResponse response) {
        OrderStatus status = response.isSuccess() ? OrderStatus.COMPLETED : OrderStatus.FAILED;
        orderService.updateStatus(response.getOrderId(), status).subscribe();
    }
}

