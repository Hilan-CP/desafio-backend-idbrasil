package com.idbrasil.idmarket.dto;

import com.idbrasil.idmarket.validations.UniqueProdutoSku;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

@UniqueProdutoSku
public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "Obrigatório informar SKU")
    private String sku;

    @NotBlank(message = "Obrigatório informar nome do produto")
    private String nome;

    @NotBlank(message = "Obrigatório informar descrição do produto")
    private String descricao;

    @NotNull(message = "Obrigatório informar preço do produto")
    @Positive(message = "Preço deve ser maior que zero")
    private Double preco;

    @NotNull(message = "Obrigatório informar estoque do produto")
    @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero")
    private Integer estoque;

    private boolean ativo;
    private Instant createdAt;
    private Instant updatedAt;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String sku, String nome, String descricao, Double preco, Integer estoque, boolean ativo, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sku = sku;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
