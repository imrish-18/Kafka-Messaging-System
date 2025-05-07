package com.inventory.service.model;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
    private String productId;
    private int quantity;
}

