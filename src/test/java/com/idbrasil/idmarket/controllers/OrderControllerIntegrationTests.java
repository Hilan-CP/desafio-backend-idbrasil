package com.idbrasil.idmarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idbrasil.idmarket.dto.OrderDTO;
import com.idbrasil.idmarket.dto.OrderItemDTO;
import com.idbrasil.idmarket.entities.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long canceledOrderId;
    private String existingSku;
    private String nonExistingSku;
    private Integer quantity;
    private Long customerId;
    private OrderStatus status;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        canceledOrderId = 4L;
        existingSku = "P-001";
        nonExistingSku = "P-1000";
        quantity = 10;
        customerId = 456L;
        status = OrderStatus.CREATED;
        orderDTO = new OrderDTO(null, customerId, null, null, null);
        orderDTO.getItems().add(new OrderItemDTO(existingSku, quantity, null));
    }

    @Test
    public void getOrderShouldReturnOkWhenExistingId() throws Exception {
        mockMvc.perform(get("/orders/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void getOrderShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(get("/orders/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPagedOrdersShouldReturnOkWhenCustomerIdParam() throws Exception {
        mockMvc.perform(get("/orders?customerId={customerId}", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void getPagedOrdersShouldReturnOkWhenStatusParam() throws Exception {
        mockMvc.perform(get("/orders?status={status}", status)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void getPagedOrdersShouldReturnOkWhenNoParam() throws Exception {
        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void createOrderShouldReturnCreatedWhenExistingSkuAndHasStock() throws Exception {
        String json = objectMapper.writeValueAsString(orderDTO);
        mockMvc.perform(post("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.toString()))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void createOrderShouldReturnNotFoundWhenNonExistingSku() throws Exception {
        orderDTO.getItems().getFirst().setProductSku(nonExistingSku);
        String json = objectMapper.writeValueAsString(orderDTO);
        mockMvc.perform(post("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createOrderShouldReturnUnprocessableEntityWhenOutOfStock() throws Exception {
        orderDTO.getItems().getFirst().setQuantity(99999);
        String json = objectMapper.writeValueAsString(orderDTO);
        mockMvc.perform(post("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateOrderToPaidShouldReturnOkWhenExistingId() throws Exception {
        mockMvc.perform(post("/orders/{id}/pay", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.PAID.toString()));
    }

    @Test
    public void updateOrderToPaidShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(post("/orders/{id}/pay", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrderToPaidShouldReturnUnprocessableEntityWhenStatusCanceled() throws Exception {
        mockMvc.perform(post("/orders/{id}/pay", canceledOrderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateOrderToCanceledShouldReturnOkWhenExistingId() throws Exception {
        mockMvc.perform(post("/orders/{id}/cancel", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED.toString()));
    }

    @Test
    public void updateOrderToCanceledShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(post("/orders/{id}/cancel", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
