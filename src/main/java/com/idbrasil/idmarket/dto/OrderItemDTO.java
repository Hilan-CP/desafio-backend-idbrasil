package com.idbrasil.idmarket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemDTO {

    @NotNull(message = "Obrigatório informar SKU do produto comprado")
    private String productSku;

    @NotNull(message = "Obrigatório informar quantidade do produto comprado")
    @Positive(message = "A quantidade do produto comprado precisa ser maior que zero")
    private Integer quantity;

    private Double price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(String productSku, Integer quantity, Double price) {
        this.productSku = productSku;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
