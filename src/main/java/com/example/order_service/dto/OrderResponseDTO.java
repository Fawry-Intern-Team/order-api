package com.example.order_service.dto;

import com.example.order_service.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    private UUID id;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String couponCode;
    private UUID transactionId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderProductResponseDTO> orderProducts;
}

