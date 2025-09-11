package com.idbrasil.idmarket.repositories;

import com.idbrasil.idmarket.entities.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {
    public static Specification<Produto> equalSku(String sku){
        return (root, query, builder) -> {
            return builder.equal(root.get("sku"), sku);
        };
    }

    public static Specification<Produto> likeNome(String nome){
        return (root, query, builder) -> {
            return builder.like(builder.upper(root.get("nome")), "%"+nome.toUpperCase()+"%");
        };
    }

    public static Specification<Produto> equalAtivo(boolean ativo){
        return (root, query, builder) -> {
            return builder.equal(root.get("ativo"), ativo);
        };
    }
}
