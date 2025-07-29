package com.example.order_service.service;

import com.example.order_service.component.OrderSpecification;
import com.example.order_service.dto.OrderProductRequestDTO;
import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.UpdateOrderStatusDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderProduct;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.NotificationEvent;
import org.example.events.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderSagaStarter orderSagaStarter;
    private final RabbitTemplate  rabbitTemplate;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Optional<Order> existing = orderRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            return orderMapper.toDto(existing.get());
        }
        Order order = orderMapper.toEntity(request);

        log.info(order.toString());
        List<OrderProduct> productEntities = new ArrayList<>();
        for (OrderProductRequestDTO dto : request.getOrderProducts()) {
            OrderProduct op = new OrderProduct();
            op.setProductId(dto.getProductId());
            op.setQuantity(dto.getQuantity());
            op.setStoreId(dto.getStoreId());
            op.setOrder(order);
            productEntities.add(op);
        }

        order.setOrderProducts(productEntities);

        Order saved = orderRepository.save(order);
        orderSagaStarter.startOrderSaga(saved);
        return orderMapper.toDto(saved);
    }


    @Override
    public OrderResponseDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(int page, int size, OrderStatus status, UUID customerId, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());


        return orderRepository.findAll(OrderSpecification.filterBy(customerId, status), pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDTO updateOrder(UUID id, UpdateOrderStatusDTO request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
        order.setStatus(request.getStatus());
        order.setTransactionId(request.getTransactionId());
        Order saved = orderRepository.save(order);
        NotificationEvent event= NotificationEvent.builder()
                .type(NotificationType.EMAIL)
                .recipient("moham35356@gmail.com")
                .content("order completed successfully")
                .subject("order completed successfully")
                .build();
        rabbitTemplate.convertAndSend("notification-queue", event);
        return orderMapper.toDto(saved);
    }
}
