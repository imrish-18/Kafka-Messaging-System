package com.rishabh.order_service.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Slf4j
@Setter
public class OrderRequestDTO {
    private String userId;
    private String productId;
    private Integer quantity;
}

