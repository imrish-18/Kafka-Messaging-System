package com.rishabh.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
@Builder
public class Order {
    @Id
    private String id;
    private String userId;
    private String name;
    private String email;
    private String productId;
    private String productName;
    private int quantity;
    private double totalPrice;
    private LocalDateTime orderDate;
}

