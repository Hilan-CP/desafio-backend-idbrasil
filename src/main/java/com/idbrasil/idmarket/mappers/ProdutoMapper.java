package com.idbrasil.idmarket.mappers;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.entities.Produto;

public class ProdutoMapper {
    public static ProdutoDTO entityToDto(Produto entity){
        return new ProdutoDTO(
                entity.getId(),
                entity.getSku(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.getEstoque(),
                entity.isAtivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Produto dtoToEntity(Produto entity, ProdutoDTO dto){
        entity.setSku(dto.getSku());
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setEstoque(dto.getEstoque());
        return entity;
    }
}
