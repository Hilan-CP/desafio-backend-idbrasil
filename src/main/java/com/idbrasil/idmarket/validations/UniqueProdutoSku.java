package com.idbrasil.idmarket.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueProdutoSkuValidator.class)
public @interface UniqueProdutoSku {

    String message() default "SKU já cadastrado para outro produto";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
