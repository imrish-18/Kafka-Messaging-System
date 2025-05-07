//package com.inventory.service.kafkaConfig;
//
//import com.inventory.service.model.InventoryRequest;
//import com.inventory.service.model.OrderEvent;
//import com.inventory.service.serviceImpl.InventoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class OrderEventListener {
//    private final InventoryService inventoryService;
//
//    @KafkaListener(topics = "order-events", groupId = "inventory-group")
//    public void handleOrderEvent(OrderEvent event) {
//        InventoryRequest request = new InventoryRequest(event.getProductId(), event.getQuantity());
//        inventoryService.deductInventory(request)
//                .subscribe(success -> {
//                    if (success) {
//                        System.out.println("Inventory deducted for order: " + event.getOrderId());
//                    } else {
//                        System.out.println("Inventory insufficient for order: " + event.getOrderId());
//                    }
//                });
//    }
//}
//
