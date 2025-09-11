package com.idbrasil.idmarket.services;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.exceptions.ResourceNotFoundException;
import com.idbrasil.idmarket.exceptions.UniqueFieldException;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import com.idbrasil.idmarket.validations.ProdutoValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
public class ProdutoServiceTests {

    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoValidator validator;

    private Long existingId;
    private Long nonExistingId;
    private String existingSku;
    private String newSku;
    private Produto produto;
    private ProdutoDTO newProdutoDto;
    private Page<Produto> pagedProdutos;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        existingSku = "P-001";
        newSku = "P-100";
        produto = new Produto(existingId, existingSku, "Teclado", "Teclado sem fio", 200.0, 2, true, null, null);
        newProdutoDto = new ProdutoDTO(null, newSku, "Novo Produto", "Nova descricao", 100.0, 20, true, null, null);
        pagedProdutos = new PageImpl<>(List.of(produto));
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(produto));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedProdutos);
        Mockito.when(repository.save(any())).thenReturn(produto);
        Mockito.doThrow(UniqueFieldException.class).when(validator).validateUniqueSku(newProdutoDto.getId(), existingSku);
        Mockito.doNothing().when(validator).validateUniqueSku(produto.getId(), existingSku);
        Mockito.doNothing().when(validator).validateUniqueSku(any(), eq(newSku));
    }

    @Test
    public void getProdutoShouldReturnProdutoDTOWhenExistingId(){
        ProdutoDTO produtoDTO = service.getProduto(existingId);
        Assertions.assertEquals(existingId, produtoDTO.getId());
    }

    @Test
    public void getProdutoShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.getProduto(nonExistingId);
        });
    }

    @Test
    public void getPagedProdutosShouldReturnPageOfProdutoDTO(){
        Page<ProdutoDTO> dtoPage = service.getPagedProdutos(null, null, null, PageRequest.ofSize(10));
        Assertions.assertFalse(dtoPage.isEmpty());
    }

    @Test
    public void createProdutoShouldReturnProdutoDTOWhenNewSku(){
        ProdutoDTO produtoDTO = service.createProduto(newProdutoDto);
        Assertions.assertNotNull(produtoDTO.getId());
    }

    @Test
    public void createProdutoShouldThrowUniqueFieldExceptionWhenExistingSku(){
        newProdutoDto.setSku(existingSku);
        Assertions.assertThrows(UniqueFieldException.class, () -> {
            service.createProduto(newProdutoDto);
        });
    }

    @Test
    public void updateProdutoShouldReturnProdutoDTOWhenExistingId(){
        ProdutoDTO produtoDTO = service.updateProduto(existingId, newProdutoDto);
        Assertions.assertNotNull(produtoDTO.getId());
    }

    @Test
    public void updateProdutoShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateProduto(nonExistingId, newProdutoDto);
        });
    }

    @Test
    public void updateProdutoShouldReturnProdutoDTOWhenSameIdAndExistingSku(){
        newProdutoDto.setSku(existingSku);
        Assertions.assertDoesNotThrow(() -> {
            service.updateProduto(existingId, newProdutoDto);
        });
    }

    @Test
    public void updateProdutoShouldThrowUniqueSkuExceptionWhenDifferentIdAndExistingSku(){
        newProdutoDto.setSku(existingSku);
        Assertions.assertThrows(UniqueFieldException.class, () -> {
            service.updateProduto(newProdutoDto.getId(), newProdutoDto);
        });
    }

    @Test
    public void updateEstoqueProdutoShouldReturnProdutoDTOWhenExistingId(){
        Assertions.assertDoesNotThrow(() -> {
            service.updateEstoqueProduto(existingId, newProdutoDto);
        });
    }

    @Test
    public void updateEstoqueProdutoShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateEstoqueProduto(nonExistingId, newProdutoDto);
        });
    }

    @Test
    public void deleteProdutoShouldDoNotThrowWhenExistingId(){
        Assertions.assertDoesNotThrow(() -> {
            service.deleteProduto(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).save(any());
    }

    @Test
    public void deleteProdutoShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteProduto(nonExistingId);
        });
        Mockito.verify(repository, Mockito.never()).save(any());
    }
}
