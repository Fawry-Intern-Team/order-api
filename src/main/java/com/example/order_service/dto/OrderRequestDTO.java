package com.example.order_service.dto;

import com.example.order_service.enums.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull
    private Long customerId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal totalAmount;

    private OrderStatus status;
    private String couponCode;
    private String transactionId;

    @NotEmpty(message = "Order must contain at least one product")
    private List<OrderProductRequestDTO> orderProducts;
}
