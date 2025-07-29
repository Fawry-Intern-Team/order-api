package com.example.order_service.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderProductResponseDTO {
    private UUID productId;
    private Integer quantity;
    private UUID storeId;
}
