package com.example.order_service.service;

import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.UpdateOrderStatusDTO;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);

    OrderResponseDTO getOrderById(Long id);

    Page<OrderResponseDTO> getAllOrders(int page, int size, String status, Long customerId, String sortBy);

    OrderResponseDTO updateOrder(Long id, UpdateOrderStatusDTO request);

    void deleteOrder(Long id);
}
