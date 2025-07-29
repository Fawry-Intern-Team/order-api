package com.example.order_service.component;

import com.example.order_service.enums.OrderStatus;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderFailedEvent;
import org.example.events.PaymentCompletedEvent;
import org.example.events.StockReservedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener {
    private final OrderRepository orderRepository;


    @RabbitListener(queues = "origin.order.failed")
    public void onOrderFailed(OrderFailedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            log.info("❌ Order " + event.getOrderId() + " marked as FAILED. Reason: " + event.getReason());
        });

    }

    @RabbitListener(queues = "coupon.applied.queue")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.PROCESSING);
            order.setTransactionId(event.getTransactionId());
            orderRepository.save(order);
            log.info("✅ Order " + event.getOrderId() + " marked as COMPLETED.");
        });
    }

}
