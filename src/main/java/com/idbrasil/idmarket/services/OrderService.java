package com.idbrasil.idmarket.services;

import com.idbrasil.idmarket.dto.OrderDTO;
import com.idbrasil.idmarket.entities.Order;
import com.idbrasil.idmarket.entities.OrderItem;
import com.idbrasil.idmarket.entities.OrderStatus;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.exceptions.ErrorMessage;
import com.idbrasil.idmarket.exceptions.OrderStatusException;
import com.idbrasil.idmarket.exceptions.OutOfStockException;
import com.idbrasil.idmarket.exceptions.ResourceNotFoundException;
import com.idbrasil.idmarket.mappers.OrderMapper;
import com.idbrasil.idmarket.repositories.OrderRepository;
import com.idbrasil.idmarket.repositories.OrderSpecification;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        Optional<Order> result = orderRepository.findById(id);
        Order order = result.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        return OrderMapper.entityToDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getPagedOrders(Long customerId, OrderStatus status, Pageable pageable) {
        Specification<Order> specification = buildSpecification(customerId, status);
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        return orders.map(order -> OrderMapper.entityToDto(order));
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = OrderMapper.dtoToEntity(new Order(), dto);
        List<Produto> produtosSold = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            Optional<Produto> foundProduto = produtoRepository.findBySku(item.getProductSku());
            Produto produto = foundProduto.orElseThrow(() -> new ResourceNotFoundException("SKU não encontrado: " + item.getProductSku()));
            if(produto.getEstoque() < item.getQuantity()){
                throw new OutOfStockException("Produto esgotado - SKU: " + item.getProductSku());
            }
            item.setPrice(produto.getPreco());
            produto.setEstoque(produto.getEstoque() - item.getQuantity());
            produtosSold.add(produto);
        }
        order.setTotal(order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order = orderRepository.save(order);
        produtoRepository.saveAll(produtosSold);
        return OrderMapper.entityToDto(order);
    }

    @Transactional
    public OrderDTO updateOrderToPaid(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        if(order.getStatus().equals(OrderStatus.CANCELLED)){
            throw new OrderStatusException(ErrorMessage.CANCELED_ORDER);
        }
        order.setStatus(OrderStatus.PAID);
        order = orderRepository.save(order);
        return OrderMapper.entityToDto(order);
    }

    @Transactional
    public OrderDTO updateOrderToCanceled(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        order.setStatus(OrderStatus.CANCELLED);
        List<Produto> produtosRestored = new ArrayList<>();
        for(OrderItem item : order.getItems()){
            Produto produto = produtoRepository.findBySku(item.getProductSku()).get();
            produto.setEstoque(produto.getEstoque() + item.getQuantity());
            produtosRestored.add(produto);
        }
        order = orderRepository.save(order);
        produtoRepository.saveAll(produtosRestored);
        return OrderMapper.entityToDto(order);
    }

    private Specification<Order> buildSpecification(Long customerId, OrderStatus status) {
        List<Specification<Order>> specifications = new ArrayList<>();
        if (customerId != null) {
            specifications.add(OrderSpecification.equalCustomerId(customerId));
        }
        if (status != null) {
            specifications.add(OrderSpecification.equalStatus(status));
        }
        return Specification.allOf(specifications);
    }
}
