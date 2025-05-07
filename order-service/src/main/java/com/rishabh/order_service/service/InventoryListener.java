package com.rishabh.order_service.service;

import com.rishabh.order_service.dto.InventoryRequest;
import com.rishabh.order_service.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryListener {

    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "inventory-events", groupId = "inventory")
    public void reserveInventory(InventoryRequest request) {
        boolean inventorySuccess = true; // mock
        kafkaTemplate.send("inventory-responses", new InventoryResponse(request.getOrderId(), inventorySuccess));
    }
}

