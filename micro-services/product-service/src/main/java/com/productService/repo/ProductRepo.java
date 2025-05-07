package com.productService.repo;

import com.productService.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends ReactiveMongoRepository<Product,String> {
}
