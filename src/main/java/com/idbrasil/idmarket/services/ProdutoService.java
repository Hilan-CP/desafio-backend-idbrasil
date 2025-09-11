package com.idbrasil.idmarket.services;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.exceptions.ErrorMessage;
import com.idbrasil.idmarket.exceptions.ResourceNotFoundException;
import com.idbrasil.idmarket.mappers.ProdutoMapper;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import com.idbrasil.idmarket.repositories.ProdutoSpecification;
import com.idbrasil.idmarket.validations.ProdutoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoValidator validator;

    @Transactional(readOnly = true)
    public ProdutoDTO getProduto(Long id) {
        Optional<Produto> result = produtoRepository.findById(id);
        Produto produto = result.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        return ProdutoMapper.entityToDto(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> getPagedProdutos(String sku, String nome, Boolean ativo, Pageable pageable) {
        Specification<Produto> specification = buildSpecification(sku, nome, ativo);
        Page<Produto> produtos = produtoRepository.findAll(specification, pageable);
        return produtos.map(produto -> ProdutoMapper.entityToDto(produto));
    }

    @Transactional
    public ProdutoDTO createProduto(ProdutoDTO dto) {
        validator.validateUniqueSku(null, dto.getSku());
        Produto entity = ProdutoMapper.dtoToEntity(new Produto(), dto);
        entity = produtoRepository.save(entity);
        return ProdutoMapper.entityToDto(entity);
    }

    @Transactional
    public ProdutoDTO updateProduto(Long id, ProdutoDTO dto) {
        validator.validateUniqueSku(id, dto.getSku());
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        entity = ProdutoMapper.dtoToEntity(entity, dto);
        entity = produtoRepository.save(entity);
        return ProdutoMapper.entityToDto(entity);
    }

    @Transactional
    public ProdutoDTO updateEstoqueProduto(Long id, ProdutoDTO dto) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        entity.setEstoque(dto.getEstoque());
        entity = produtoRepository.save(entity);
        return ProdutoMapper.entityToDto(entity);
    }

    @Transactional
    public void deleteProduto(Long id) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
        entity.setAtivo(false);
        produtoRepository.save(entity);
    }

    private Specification<Produto> buildSpecification(String sku, String nome, Boolean ativo) {
        List<Specification<Produto>> specifications = new ArrayList<>();
        if (sku != null && !sku.isBlank()) {
            specifications.add(ProdutoSpecification.equalSku(sku));
        }
        if (nome != null && !nome.isBlank()) {
            specifications.add(ProdutoSpecification.likeNome(nome));
        }
        if (ativo != null) {
            specifications.add(ProdutoSpecification.equalAtivo(ativo));
        }
        return Specification.allOf(specifications);
    }
}
