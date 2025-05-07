package com.rishabh.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {
    private String orderId;
    private String userId;
    private double totalPrice;
    private String status; // NEW, PAYMENT_COMPLETED, PAYMENT_FAILED
    private LocalDateTime eventTime;
}

