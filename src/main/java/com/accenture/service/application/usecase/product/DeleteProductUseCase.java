package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.ProductNotFoundException;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Eliminar un producto de una sucursal.
 */
public class DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DeleteProductUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Mono<Void> execute(Long productId) {
        if (productId == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto es obligatorio"));
        }

        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(product -> productRepositoryPort.deleteById(product.getId()));
    }
}
