package com.inventory.service.serviceImpl;

import com.inventory.service.model.Inventory;
import com.inventory.service.model.InventoryRequest;
import com.inventory.service.repo.InventoryRepository;
import com.rishabh.order.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {


    @Autowired
    private  InventoryRepository inventoryRepository;

    @Autowired
    private KafkaTemplate<String,OrderEvent> kafkaTemplate;

    public Mono<Boolean> checkAndReduceInventory(String productId, int qty) {
        return inventoryRepository.findByProductId(productId)
                .flatMap(inventory -> {
                    if (inventory.getQuantity() >= qty) {
                        inventory.setQuantity(inventory.getQuantity() - qty);
                        return inventoryRepository.save(inventory).thenReturn(true);
                    }
                    return Mono.just(false);
                })
                .defaultIfEmpty(false);
    }

    public Flux<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Mono<Boolean> deductInventory(InventoryRequest request) {
        return inventoryRepository.findByProductId(request.getProductId())
                .flatMap(inventory -> {
                    if (inventory.getQuantity() >= request.getQuantity()) {
                        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
                        return inventoryRepository.save(inventory).thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                })
                .defaultIfEmpty(false);
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderEvent(OrderEvent event) {
        log.info("Received OrderEvent in InventoryService: {}", event);

        inventoryRepository.findByProductId(event.getOrderId()) // or use productId in event
                .flatMap(inventory -> {
                    if (inventory.getQuantity() >= 1) {
                        inventory.setQuantity(inventory.getQuantity() - 1);
                        return inventoryRepository.save(inventory);
                    }
                    return Mono.empty(); // or send failure event
                })
                .subscribe(saved -> {
                    OrderEvent inventoryConfirmedEvent = OrderEvent.builder()
                            .orderId(event.getOrderId())
                            .userId(event.getUserId())
                            .totalPrice(event.getTotalPrice())
                            .status("INVENTORY_DEDUCTED")
                            .eventTime(LocalDateTime.now())
                            .build();

                    // Use KafkaTemplate to send this back to another topic (like "inventory-status")
                    kafkaTemplate.send("inventory-status", inventoryConfirmedEvent);
                });

        // Extend: publish another Kafka event or update DB as needed
    }
//    @KafkaListener(topics = "order-events", groupId = "inventory-group")
//    public void handleSagaOrderEvent(OrderEvent event) {
//        log.info("InventoryService received OrderEvent: {}", event);
//
//        inventoryRepository.findByProductId(event.getOrderId()) // or use productId in event
//                .flatMap(inventory -> {
//                    if (inventory.getQuantity() >= 1) {
//                        inventory.setQuantity(inventory.getQuantity() - 1);
//                        return inventoryRepository.save(inventory);
//                    }
//                    return Mono.empty(); // or send failure event
//                })
//                .subscribe(saved -> {
//                    OrderEvent inventoryConfirmedEvent = OrderEvent.builder()
//                            .orderId(event.getOrderId())
//                            .userId(event.getUserId())
//                            .totalPrice(event.getTotalPrice())
//                            .status("INVENTORY_DEDUCTED")
//                            .eventTime(LocalDateTime.now())
//                            .build();
//
//                    // Use KafkaTemplate to send this back to another topic (like "inventory-status")
//                    kafkaTemplate.send("inventory-status", inventoryConfirmedEvent);
//                });
//    }

}

