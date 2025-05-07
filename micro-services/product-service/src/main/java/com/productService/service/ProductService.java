package com.productService.service;

import com.productService.model.Product;
import com.productService.repo.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private RedissonClient redissonClient;

    public Mono<Product> saveProduct(Product product){
        return productRepo.save(product).doOnNext(prod->{
            String productKey="product:"+prod.getProductId();
            redissonClient.getBucket(productKey).delete();
            log.info("user details has been cleared from cache...for the key {} ",prod.getProductId());
            String allProductKey="product:all";
            redissonClient.getBucket(allProductKey).delete();
            log.info("user details has been cleared from cache...for the all key {} ",allProductKey);
            log.info("user details has been saved into db successfully..");
        });
    }


    public Mono<Product> getProductById(String Id){
        String productKey="product:"+Id;
        Product cachedProduct=redissonClient.<Product>getBucket(productKey).get();
        if(cachedProduct!=null){
            log.info("this is coming from cache..");
            return Mono.just(cachedProduct);
        }
        else {
            log.info("cache miss. this is coming from db call...");
            return productRepo.findById(Id).flatMap(product->{
                redissonClient.getBucket(productKey).set(product, Duration.ofSeconds(60));
                return Mono.just(product);
            });
        }
    }

    public Flux<Product> getAllUsers() {
        String key = "product:all";

        List<Product> cachedUsers = redissonClient.<List<Product>>getBucket(key).get();

        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            log.info("this is coming from cache....");
            return Flux.fromIterable(cachedUsers);
        } else {
            log.info("this is coming from db ...");
            return productRepo.findAll()
                    .collectList()
                    .doOnNext(users -> {
                        if (!users.isEmpty()) {
                            redissonClient.<List<Product>>getBucket(key).set(users, Duration.ofSeconds(60));
                        }
                    })
                    .flatMapMany(Flux::fromIterable);
        }
    }
}
