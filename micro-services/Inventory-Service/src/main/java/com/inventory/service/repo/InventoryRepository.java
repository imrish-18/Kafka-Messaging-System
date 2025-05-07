package com.inventory.service.repo;

import com.inventory.service.model.Inventory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InventoryRepository extends ReactiveMongoRepository<Inventory, String> {
    Mono<Inventory> findByProductId(String productId);
}

