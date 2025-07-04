package com.example.order_service.dto;

import com.example.order_service.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}
