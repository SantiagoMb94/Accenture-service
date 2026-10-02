package com.accenture.service.domain.exception;

/**
 * Excepción lanzada cuando un producto solicitado no existe.
 */
public class ProductNotFoundException extends BusinessRuleException {

    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super("No se encontró el producto con ID: " + productId);
        this.productId = productId;
    }

    public ProductNotFoundException(String message) {
        super(message);
        this.productId = null;
    }

    public Long getProductId() {
        return productId;
    }
}
