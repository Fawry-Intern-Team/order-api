package com.example.order_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderProductRequestDTO {
    @NotNull(message = "orderId is required")
    private UUID orderId;
    @NotNull(message = "productId is required")
    private UUID productId;
    @NotNull(message = "storeId is required")
    private UUID storeId;
    @NotNull(message = "quantity is required")
    @Min(value = 1,message = "Quantity must be at least 1")
    private Integer quantity;


}
