package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderProductRequestDTO {
    @NotNull(message = "orderId is required")
    private Long orderId;
    @NotNull(message = "productId is required")
    private Long productId;
    @NotNull(message = "quantity is required")
    @Min(value = 1,message = "Quantity must be at least 1")
    private Integer quantity;
}
