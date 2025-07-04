package com.example.order_service.mapper;


import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.entity.Order;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderRequestDTO dto);

    OrderResponseDTO toDto(Order order);

}
