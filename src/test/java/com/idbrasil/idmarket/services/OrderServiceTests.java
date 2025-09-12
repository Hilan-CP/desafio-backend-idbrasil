package com.idbrasil.idmarket.services;

import com.idbrasil.idmarket.dto.OrderDTO;
import com.idbrasil.idmarket.dto.OrderItemDTO;
import com.idbrasil.idmarket.entities.Order;
import com.idbrasil.idmarket.entities.OrderItem;
import com.idbrasil.idmarket.entities.OrderStatus;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.exceptions.OrderStatusException;
import com.idbrasil.idmarket.exceptions.OutOfStockException;
import com.idbrasil.idmarket.exceptions.ResourceNotFoundException;
import com.idbrasil.idmarket.repositories.OrderRepository;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    private Long existingId;
    private Long nonExistingId;
    private String existingSku;
    private String nonExistingSku;
    private String outOfStockSku;
    private OrderItem orderItem;
    private Order order;
    private Produto produto;
    private Produto outOfStockProduto;
    private Page<Order> pagedOrders;
    private OrderDTO newOrderDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        existingSku = "P-001";
        nonExistingSku = "P-1000";
        outOfStockSku = "P-002";
        order = new Order(existingId, 456L, OrderStatus.CREATED, 1000.0, null);
        orderItem = new OrderItem(1L, existingSku, 10, 100.0, order);
        order.getItems().add(orderItem);
        pagedOrders = new PageImpl<>(List.of(order));
        produto = new Produto(1L, existingSku, "Nome Produto", "Descricao", 100.0, 1000, true, null, null);
        outOfStockProduto = new Produto(2L, outOfStockSku, "Produto sem estoque", "Descricao", 100.0, 0, true, null, null);
        newOrderDTO = new OrderDTO(null, 456L, null, null, null);
        newOrderDTO.getItems().add(new OrderItemDTO(existingSku, 10, null));
        Mockito.when(orderRepository.findById(existingId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedOrders);
        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(produtoRepository.findBySku(existingSku)).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.findBySku(outOfStockSku)).thenReturn(Optional.of(outOfStockProduto));
        Mockito.when(produtoRepository.findBySku(nonExistingSku)).thenReturn(Optional.empty());
        Mockito.when(produtoRepository.saveAll(any())).thenReturn(List.of(produto));
    }

    @Test
    public void getOrderShouldReturnOrderDTOWhenExistingId() {
        OrderDTO orderDTO = service.getOrder(existingId);
        Assertions.assertEquals(existingId, orderDTO.getId());
    }

    @Test
    public void getOrderShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.getOrder(nonExistingId);
        });
    }

    @Test
    public void getPagedOrdersShouldReturnPageOfOrderDTO() {
        Page<OrderDTO> dtoPage = service.getPagedOrders(null, null, PageRequest.ofSize(10));
        Assertions.assertFalse(dtoPage.isEmpty());
    }

    @Test
    public void createOrderShouldReturnOrderDTOWhenExistingSkuAndHasStock() {
        OrderDTO orderDTO = service.createOrder(newOrderDTO);
        Assertions.assertEquals(1000.0, orderDTO.getTotal());
        Assertions.assertEquals(990, produto.getEstoque());
    }

    @Test
    public void createOrderShouldThrowResourceNotFoundExceptionWhenNonExistingSku() {
        newOrderDTO.getItems().getFirst().setProductSku(nonExistingSku);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.createOrder(newOrderDTO);
        });
    }

    @Test
    public void createOrderShouldThrowOutOfStockExceptionWhenOutOfStockSku() {
        newOrderDTO.getItems().getFirst().setProductSku(outOfStockSku);
        Assertions.assertThrows(OutOfStockException.class, () -> {
            service.createOrder(newOrderDTO);
        });
    }

    @Test
    public void updateOrderToPaidShouldReturnOrderDTOWhenExistingIdAndCreated() {
        OrderDTO orderDTO = service.updateOrderToPaid(existingId);
        Assertions.assertEquals(OrderStatus.PAID, orderDTO.getStatus());
    }

    @Test
    public void updateOrderToPaidShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateOrderToPaid(nonExistingId);
        });
    }

    @Test
    public void updateOrderToPaidShouldThrowOrderStatusExceptionWhenCanceled() {
        order.setStatus(OrderStatus.CANCELLED);
        Assertions.assertThrows(OrderStatusException.class, () -> {
            service.updateOrderToPaid(existingId);
        });
    }

    @Test
    public void updateOrderToCanceledShouldReturnOrderDTOWhenExistingId() {
        OrderDTO orderDTO = service.updateOrderToCanceled(existingId);
        Assertions.assertEquals(OrderStatus.CANCELLED, orderDTO.getStatus());
    }

    @Test
    public void updateOrderToCanceledShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateOrderToCanceled(nonExistingId);
        });
    }
}
