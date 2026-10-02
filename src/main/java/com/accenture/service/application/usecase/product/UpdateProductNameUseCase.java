package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.ProductNotFoundException;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Modificar el nombre de un producto existente.
 */
public class UpdateProductNameUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public UpdateProductNameUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Mono<Product> execute(Long productId, String newName) {
        if (productId == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto es obligatorio"));
        }
        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nuevo nombre del producto no puede estar vacío"));
        }

        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .map(product -> product.withName(newName.trim()))
                .flatMap(productRepositoryPort::save);
    }
}
