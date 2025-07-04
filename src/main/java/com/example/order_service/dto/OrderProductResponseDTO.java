package com.example.order_service.dto;

import lombok.Data;

@Data
public class OrderProductResponseDTO {
    private Long productId;
    private Integer quantity;
}
