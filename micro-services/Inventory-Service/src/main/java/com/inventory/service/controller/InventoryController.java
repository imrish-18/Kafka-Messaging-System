package com.inventory.service.controller;

import com.inventory.service.model.Inventory;
import com.inventory.service.model.InventoryRequest;
import com.inventory.service.repo.InventoryRepository;
import com.inventory.service.serviceImpl.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private final InventoryService inventoryService;


    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public Flux<Inventory> getAll() {
        return inventoryService.getAllInventory();
    }

    @PostMapping("/check-and-reduce")
    public Mono<Boolean> checkAndReduce(@RequestParam String productId, @RequestParam int quantity) {
        return inventoryService.checkAndReduceInventory(productId, quantity);
    }
    @PostMapping("/deduct")
    public Mono<String> deduct(@RequestBody InventoryRequest request) {
        return inventoryRepository.findByProductId(request.getProductId())
                .flatMap(inventory -> {
                    if (inventory.getQuantity() >= request.getQuantity()) {
                        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
                        return inventoryRepository.save(inventory).thenReturn("Inventory deducted");
                    } else {
                        return Mono.error(new RuntimeException("Insufficient inventory"));
                    }
                });
    }

    @PostMapping("/restock")
    public Mono<String> restock(@RequestBody InventoryRequest request) {
        return inventoryRepository.findByProductId(request.getProductId())
                .flatMap(inventory -> {
                    inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
                    return inventoryRepository.save(inventory).thenReturn("Inventory restocked");
                });
    }

    // Save or update inventory
    @PostMapping("/save")
    public Mono<String> saveInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.findByProductId(inventory.getProductId())
                .flatMap(existing -> {
                    existing.setQuantity(inventory.getQuantity());
                    return inventoryRepository.save(existing);
                })
                .switchIfEmpty(inventoryRepository.save(inventory))
                .thenReturn("Inventory saved successfully");
    }

    @PostMapping("/deduct-invent")
    public Mono<Boolean> deductInventory(@RequestBody InventoryRequest request) {
        return inventoryService.deductInventory(request);
    }
}

