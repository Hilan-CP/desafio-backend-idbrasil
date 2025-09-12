package com.idbrasil.idmarket.controllers;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.dto.ProdutoEstoqueDTO;
import com.idbrasil.idmarket.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> getProduto(@PathVariable Long id) {
        ProdutoDTO produto = service.getProduto(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> getPagedProdutos(
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        Page<ProdutoDTO> produtos = service.getPagedProdutos(sku, nome, ativo, pageable);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> createProduto(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoDTO produto = service.createProduto(dto);
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(dto.getId());
        return ResponseEntity.created(path).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> updateProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        ProdutoDTO produto = service.updateProduto(id, dto);
        return ResponseEntity.ok(produto);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProdutoDTO> updateEstoqueProduto(@PathVariable Long id, @Valid @RequestBody ProdutoEstoqueDTO dto) {
        ProdutoDTO produto = service.updateEstoqueProduto(id, dto);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }
}
