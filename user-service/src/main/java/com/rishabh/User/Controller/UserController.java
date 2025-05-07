//package com.rishabh.User.Controller;
//
//import com.rishabh.User.Respository.UserRepository;
//import com.rishabh.User.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    @Autowired
//    private  UserRepository userRepository;
//
//    @PostMapping("/save")
//    public Mono<User> createUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }
//
//    @GetMapping
//    public Flux<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<User> getUserById(@PathVariable String id) {
//        return userRepository.findById(id);
//    }
//
//
//}
//
//
