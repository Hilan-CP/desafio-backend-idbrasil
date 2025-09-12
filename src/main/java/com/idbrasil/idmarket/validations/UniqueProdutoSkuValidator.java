package com.idbrasil.idmarket.validations;

import com.idbrasil.idmarket.dto.ProdutoDTO;
import com.idbrasil.idmarket.entities.Produto;
import com.idbrasil.idmarket.repositories.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;

public class UniqueProdutoSkuValidator implements ConstraintValidator<UniqueProdutoSku, ProdutoDTO> {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(ProdutoDTO produto, ConstraintValidatorContext context) {
        if(produto.getSku() == null || produto.getSku().isBlank()){
            return true; //validado por @NotBlank
        }
        Optional<Produto> foundProduto = repository.findBySku(produto.getSku());
        Long id = getIdPathVariable();
        if(skuWasFoundOnOtherProduct(id, foundProduto)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("sku")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private Long getIdPathVariable(){
        Map<String, String> uriVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(uriVariables.getOrDefault("id", "0"));
    }

    private boolean skuWasFoundOnOtherProduct(Long id, Optional<Produto> produto){
        return produto.isPresent() && !produto.get().getId().equals(id);
    }
}
