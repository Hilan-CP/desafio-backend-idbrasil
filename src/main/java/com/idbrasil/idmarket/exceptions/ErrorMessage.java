package com.idbrasil.idmarket.exceptions;

public class ErrorMessage {
    public static final String RESOURCE_NOT_FOUND = "Recurso não encontrado";
    public static final String UNIQUE_SKU_VIOLATION = "O SKU informado já está cadastrado para outro produto";
    public static final String CANCELED_ORDER_CANNOT_BE_PAID = "O pedido informado está cancelado e não pode ser pago";
    public static final String CANCELED_ORDER = "O pedido já está cancelado";
    public static final String PAID_ORDER = "O pedido já está pago";
}
