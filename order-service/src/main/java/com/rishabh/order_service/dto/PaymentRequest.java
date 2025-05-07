package com.rishabh.order_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public class PaymentRequest {
        private String orderId;
        private String userId;
        private Double amount;
    }


