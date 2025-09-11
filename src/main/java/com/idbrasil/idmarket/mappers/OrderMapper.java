package com.idbrasil.idmarket.mappers;

import com.idbrasil.idmarket.dto.OrderDTO;
import com.idbrasil.idmarket.dto.OrderItemDTO;
import com.idbrasil.idmarket.entities.Order;
import com.idbrasil.idmarket.entities.OrderItem;

import java.util.List;

public class OrderMapper {
    public static OrderDTO entityToDto(Order entity) {
        OrderDTO dto = new OrderDTO(
                entity.getId(),
                entity.getCustomerId(),
                entity.getStatus(),
                entity.getTotal(),
                entity.getCreatedAt()
        );
        List<OrderItemDTO> itemsDTO = entity.getItems().stream()
                .map(item -> OrderItemMapper.entityToDto(item))
                .toList();
        dto.getItems().addAll(itemsDTO);
        return dto;
    }

    public static Order dtoToEntity(Order entity, OrderDTO dto) {
        entity.setCustomerId(dto.getCustomerId());
        List<OrderItem> items = dto.getItems().stream()
                .map(itemDTO -> {
                    OrderItem item = OrderItemMapper.dtoToEntity(new OrderItem(), itemDTO);
                    item.setOrder(entity);
                    return item;
                })
                .toList();
        entity.getItems().addAll(items);
        return entity;
    }
}
