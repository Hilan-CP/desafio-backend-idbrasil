package com.idbrasil.idmarket.mappers;

import com.idbrasil.idmarket.dto.OrderItemDTO;
import com.idbrasil.idmarket.entities.OrderItem;

public class OrderItemMapper {
    public static OrderItemDTO entityToDto(OrderItem entity){
        return new OrderItemDTO(
                entity.getProductSku(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }

    public static OrderItem dtoToEntity(OrderItem entity, OrderItemDTO dto){
        entity.setProductSku(dto.getProductSku());
        entity.setQuantity(dto.getQuantity());
        return entity;
    }
}
