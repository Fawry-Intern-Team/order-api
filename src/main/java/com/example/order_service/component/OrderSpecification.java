package com.example.order_service.component;

import com.example.order_service.entity.Order;
import com.example.order_service.enums.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderSpecification {
    public static Specification<Order> filterBy(UUID customerId, OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customerId"), customerId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
