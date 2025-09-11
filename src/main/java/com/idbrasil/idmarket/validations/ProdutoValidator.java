package com.idbrasil.idmarket.validations;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.exceptions.ErrorMessage;
import com.idbrasil.idmarket.exceptions.UniqueFieldException;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import com.idbrasil.idmarket.repositories.ProdutoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoValidator {

    @Autowired
    private ProdutoRepository repository;

    public void validateUniqueSku(Long id, String sku){
        List<Produto> produtos = repository.findAll(ProdutoSpecification.equalSku(sku));
        if(!produtos.isEmpty() && !produtos.getFirst().getId().equals(id)){
            throw new UniqueFieldException(ErrorMessage.UNIQUE_SKU_VIOLATION);
        }
    }
}
