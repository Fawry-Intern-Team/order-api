package com.example.order_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
public class TestController {
    private final Producer producer;

    public TestController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/test")
    public String test() {
        MessageDTO message = new MessageDTO();
        message.setCustomerId("customer123");
        message.setOrderId("order123");
        message.setProductId("product123");
        message.setQuantity(2);
        message.setStatus("CREATED");
        producer.sendMessage(message);
        return "Order service is running!";
    }

}
