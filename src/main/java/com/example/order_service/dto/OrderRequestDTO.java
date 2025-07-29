package com.example.order_service.dto;

import com.example.order_service.enums.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {

    @NotNull
    private UUID customerId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal totalAmount;

    private OrderStatus status;
    private String couponCode;
    private UUID transactionId;
    @NotBlank(message = "idempotencyKey is required")
    private String idempotencyKey;
    @NotEmpty(message = "Order must contain at least one product")
    private List<OrderProductRequestDTO> orderProducts;
}
