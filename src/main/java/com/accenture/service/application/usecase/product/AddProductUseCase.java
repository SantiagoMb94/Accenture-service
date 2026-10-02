package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.exception.InvalidStockException;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Agregar un nuevo producto con su stock inicial a una sucursal específica.
 */
public class AddProductUseCase {

    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public AddProductUseCase(BranchRepositoryPort branchRepositoryPort,
                             ProductRepositoryPort productRepositoryPort) {
        this.branchRepositoryPort = branchRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    public Mono<Product> execute(Long branchId, String name, Integer initialStock) {
        if (branchId == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal es obligatorio"));
        }
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto no puede estar vacío"));
        }
        if (initialStock == null || initialStock < 0) {
            return Mono.error(new InvalidStockException("El stock inicial debe ser mayor o igual a cero"));
        }

        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> new Product(branch.getId(), name.trim(), initialStock))
                .flatMap(productRepositoryPort::save);
    }
}
