package com.idbrasil.idmarket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProdutoEstoqueDTO {

    @NotNull(message = "Obrigatório informar estoque do produto")
    @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero")
    private Integer estoque;

    public ProdutoEstoqueDTO() {
    }

    public ProdutoEstoqueDTO(Integer estoque) {
        this.estoque = estoque;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
