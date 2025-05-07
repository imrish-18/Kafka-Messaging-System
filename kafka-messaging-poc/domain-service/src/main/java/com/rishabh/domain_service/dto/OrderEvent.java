package com.rishabh.domain_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String message;
    private String orderStatus;  // e.g., PENDING, COMPLETED, CANCELLED
    private LocalDateTime eventDate;  // optional: timestamp of the event
    private Order order;
}

