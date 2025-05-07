package com.inventory.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalPrice;
    private String orderStatus;  // e.g., PENDING, COMPLETED, CANCELLED
    private LocalDateTime eventDate;
    private String status; // NEW, PAYMENT_COMPLETED, PAYMENT_FAILED
}

