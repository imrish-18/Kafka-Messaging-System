package com.rishabh.order_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentResponse {
    private String orderId;
    private String userId;
    private boolean success;
    private String productId;
    private Integer quantity;
}
