package com.rishabh.payment.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    private String orderId;
    private double amount;
    private String userId;
}
