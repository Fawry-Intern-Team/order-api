package com.example.order_service.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderSagaStarter orderSagaStarter;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Order order = orderMapper.toEntity(request);
        logger.info(order.toString());
        // 🧠 Set parent order in each OrderProduct
        List<OrderProduct> productEntities = new ArrayList<>();
        for (OrderProductRequestDTO dto : request.getOrderProducts()) {
            OrderProduct op = new OrderProduct();
            op.setProductId(dto.getProductId());
            op.setQuantity(dto.getQuantity());
            op.setOrder(order);
            productEntities.add(op);
        }

        order.setOrderProducts(productEntities);

        Order saved = orderRepository.save(order);
        orderSagaStarter.startOrderSaga(saved);
        return orderMapper.toDto(saved);
    }


    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(int page, int size, String status, Long customerId, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Specification<Order> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), OrderStatus.valueOf(status)));
        }

        if (customerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("customerId"), customerId));
        }

        return orderRepository.findAll(spec, pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, UpdateOrderStatusDTO request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
        order.setStatus(request.getStatus());
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }
}
