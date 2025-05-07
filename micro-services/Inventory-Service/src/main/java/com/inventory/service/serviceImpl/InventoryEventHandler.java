//package com.inventory.service.serviceImpl;
//
//import com.inventory.service.model.OrderEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class InventoryEventHandler {
//
//    @KafkaListener(topics = "order-events", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
//    public void handleOrderEvent(OrderEvent event) {
//        log.info("Received OrderEvent in InventoryService: {}", event);
//
//        // Sample logic: simulate inventory check
//        if (event.getQuantity() <= 10) {
//            log.info("Inventory available for product {}", event.getProductId());
//        } else {
//            log.warn("Insufficient inventory for product {}", event.getProductId());
//        }
//
//        // Extend: publish another Kafka event or update DB as needed
//    }
//}
//
