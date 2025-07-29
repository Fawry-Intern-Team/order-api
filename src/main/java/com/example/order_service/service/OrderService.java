package com.example.order_service.service;

import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.UpdateOrderStatusDTO;
import com.example.order_service.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);

    OrderResponseDTO getOrderById(UUID id);

    Page<OrderResponseDTO> getAllOrders(int page, int size, OrderStatus status, UUID customerId, String sortBy);

    OrderResponseDTO updateOrder(UUID id, UpdateOrderStatusDTO request);

    void deleteOrder(UUID id);
}
