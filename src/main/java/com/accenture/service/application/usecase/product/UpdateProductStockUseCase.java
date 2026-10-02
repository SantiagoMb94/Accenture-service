package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.InvalidStockException;
import com.accenture.service.domain.exception.ProductNotFoundException;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Modificar el stock de un producto (actualización de inventario).
 */
public class UpdateProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public UpdateProductStockUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Mono<Product> execute(Long productId, Integer newStock) {
        if (productId == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto es obligatorio"));
        }
        if (newStock == null || newStock < 0) {
            return Mono.error(new InvalidStockException("El stock no puede ser nulo ni negativo. Valor recibido: " + newStock));
        }

        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .map(product -> product.withStock(newStock))
                .flatMap(productRepositoryPort::save);
    }
}
