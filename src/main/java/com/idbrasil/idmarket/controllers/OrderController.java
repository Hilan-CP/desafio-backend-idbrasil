package com.idbrasil.idmarket.controllers;

import com.idbrasil.idmarket.dto.OrderDTO;
import com.idbrasil.idmarket.entities.OrderStatus;
import com.idbrasil.idmarket.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO order = service.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getPagedOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable
    ) {
        Page<OrderDTO> orders = service.getPagedOrders(customerId, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
        OrderDTO order = service.createOrder(dto);
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(dto.getId());
        return ResponseEntity.created(path).body(order);
    }
}
