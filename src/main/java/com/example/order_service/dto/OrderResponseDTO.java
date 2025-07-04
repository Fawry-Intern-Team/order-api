package com.example.order_service.dto;

import com.example.order_service.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private BigDecimal totalAmount;
    private String couponCode;
    private String transactionId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderProductResponseDTO> orderProducts;
}

