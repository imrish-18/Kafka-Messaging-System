package com.rishabh.order_service.model;

import com.rishabh.order_service.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
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
    private String productId;
    private Integer quantity;
    private OrderStatus status;
}

