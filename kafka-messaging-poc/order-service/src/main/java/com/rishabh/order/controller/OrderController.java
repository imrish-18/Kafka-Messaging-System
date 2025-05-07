package com.rishabh.order.controller;


import com.rishabh.domain_service.dto.Order;
import com.rishabh.domain_service.dto.OrderEvent;
import com.rishabh.order.kafka.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderProducer orderProducer;

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order){
      order.setOrderId(UUID.randomUUID().toString());
     order.setOrderDate(LocalDateTime.now());

        OrderEvent orderEvent=new OrderEvent();
        orderEvent.setOrderStatus("Pending");
        orderEvent.setMessage("order is in pending state");
        orderEvent.setOrder(order);
        orderEvent.setEventDate(order.getOrderDate());
        orderProducer.sendMessage(orderEvent);
        return "order place successfully....";
    }
}
