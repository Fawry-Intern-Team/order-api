package com.example.order_service.mapper;

import com.example.order_service.dto.OrderProductRequestDTO;
import com.example.order_service.dto.OrderProductResponseDTO;
import com.example.order_service.entity.OrderProduct;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    OrderProduct toEntity(OrderProductRequestDTO dto);
    OrderProductResponseDTO toDto(OrderProduct entity);

    List<OrderProduct> toEntityList(List<OrderProductRequestDTO> dtoList);
    List<OrderProductResponseDTO> toDtoList(List<OrderProduct> entityList);
}
