package com.idbrasil.idmarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.dto.ProdutoEstoqueDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProdutoControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private String existingSku;
    private String otherSku;
    private String newSku;
    private String nome;
    private boolean ativo;
    private Integer estoque;
    private ProdutoDTO produtoDTO;
    private ProdutoEstoqueDTO produtoEstoqueDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        existingSku = "P-001";
        otherSku = "P-002";
        newSku = "P-100";
        nome = "te";
        ativo = true;
        estoque = 200;
        produtoDTO = new ProdutoDTO(null, newSku, "Novo Produto", "Nova Descricao", 1000.0, 5, true, null, null);
        produtoEstoqueDTO = new ProdutoEstoqueDTO(estoque);
    }

    @Test
    public void getProdutoShouldReturnOkWhenExistingId() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void getProdutoShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPagedProdutosShouldReturnOkWhenSkuParam() throws Exception {
        mockMvc.perform(get("/products?sku={sku}", existingSku)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    public void getPagedProdutosShouldReturnOkWhenNomeParam() throws Exception {
        mockMvc.perform(get("/products?nome={nome}", nome)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void getPagedProdutosShouldReturnOkWhenAtivoParam() throws Exception {
        mockMvc.perform(get("/products?ativo={ativo}", ativo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void getPagedProdutosShouldReturnOkWhenNoParam() throws Exception {
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void createProdutoShouldReturnCreatedWhenNewSku() throws Exception {
        produtoDTO.setSku(newSku);
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void createProdutoShouldReturnUnprocessableEntityWhenExistingSku() throws Exception {
        produtoDTO.setSku(existingSku);
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateProdutoShouldReturnOkWhenExistingIdAndSameSku() throws Exception {
        produtoDTO.setSku(existingSku);
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(put("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void updateProdutoShouldReturnOkWhenExistingIdAndNewSku() throws Exception {
        produtoDTO.setSku(newSku);
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(put("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void updateProdutoShouldReturnUnprocessableEntityWhenExistingIdAndOtherSku() throws Exception {
        produtoDTO.setSku(otherSku);
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(put("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateProdutoShouldReturnNotFoundWhenNonExistingId() throws Exception {
        String json = objectMapper.writeValueAsString(produtoDTO);
        mockMvc.perform(put("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEstoqueProdutoShouldReturnOkWhenExistingId() throws Exception {
        String json = objectMapper.writeValueAsString(produtoEstoqueDTO);
        mockMvc.perform(patch("/products/{id}/stock", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estoque").value(estoque))
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void updateEstoqueProdutoShouldReturnNotFoundWhenNonExistingId() throws Exception {
        String json = objectMapper.writeValueAsString(produtoEstoqueDTO);
        mockMvc.perform(patch("/products/{id}/stock", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProdutoShouldReturnNoContentWhenExistingId() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProdutoShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
