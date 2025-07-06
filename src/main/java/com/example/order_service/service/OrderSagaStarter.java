package com.example.order_service.service;

import com.example.order_service.entity.Order;
import lombok.RequiredArgsConstructor;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderItemDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSagaStarter {

    private final RabbitTemplate rabbitTemplate;

    public void startOrderSaga(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setCustomerId(order.getCustomerId());
        event.setCouponCode(order.getCouponCode());
        event.setItems(order.getOrderProducts()
                .stream()
                .map(p -> new OrderItemDTO(p.getProductId(), p.getQuantity()))
                .toList());

        rabbitTemplate.convertAndSend("order.created.queue", event);
    }
}
