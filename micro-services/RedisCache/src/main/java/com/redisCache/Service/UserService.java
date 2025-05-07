package com.redisCache.Service;

import com.redisCache.Respository.UserRepository;
import com.redisCache.model.User;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Cacheable
    public Mono<User> getUserById(String id) {
        String key = "user:" + id;

        User cachedUser = redissonClient.<User>getBucket(key).get();
        if (cachedUser != null) {
            log.info("this is coming from cache..");
            return Mono.just(cachedUser);
        } else {
            log.info("cache miss. this is coming from db call...");
            return userRepository.findById(id)
                    .flatMap(user -> {
                        redissonClient.<User>getBucket(key).set(user);
                        return Mono.just(user);
                    });
        }
    }


    public Flux<User> getAllUsers() {
        String key = "users:all";

        List<User> cachedUsers = redissonClient.<List<User>>getBucket(key).get();

        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            log.info("this is coming from cache....");
            return Flux.fromIterable(cachedUsers);
        } else {
            log.info("this is coming from db ...");
            return userRepository.findAll()
                    .collectList()
                    .doOnNext(users -> {
                        if (!users.isEmpty()) {
                            redissonClient.<List<User>>getBucket(key).set(users, Duration.ofSeconds(60));
                        }
                    })
                    .flatMapMany(Flux::fromIterable);
        }
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user)
                .doOnNext(savedUser -> {
                    String userKey = "user:" + savedUser.getUserId();
                    redissonClient.<User>getBucket(userKey)
                            .set(savedUser, Duration.ofSeconds(60)); // TTL of 10 minutes

                    String allUsersKey = "users:all";
                    redissonClient.getBucket(allUsersKey).delete(); // Invalidate the list cache

                    log.info("User cached with TTL and user list cache cleared.");
                });
    }
//    @Cacheable(value = "users", key = "#id")
//    public Mono<User> getUserByIdEhCache(String id) {
//        return userRepository.findById(id);
//    }
//
//    @CacheEvict(value = "users", key = "#user.id")
//    public Mono<User> saveUserEhCache(User user) {
//        return userRepository.save(user);
//    }
//
//    @Cacheable(value = "usersAll")
//    public Flux<User> getAllUsersEhCache() {
//        return userRepository.findAll();
//    }
//
//    @CacheEvict(value = "usersAll", allEntries = true)
//    public Mono<User> saveUserEhCach(User user) {
//        return userRepository.save(user);
//    }

}

