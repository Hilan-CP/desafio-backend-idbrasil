package com.idbrasil.idmarket.repositories;

import com.idbrasil.idmarket.entities.Order;
import com.idbrasil.idmarket.entities.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<Order> equalCustomerId(Long customerId){
        return (root, query, builder) -> {
            return builder.equal(root.get("customerId"), customerId);
        };
    }

    public static Specification<Order> equalStatus(OrderStatus status){
        return (root, query, builder) -> {
            return builder.equal(root.get("status"), status);
        };
    }
}
