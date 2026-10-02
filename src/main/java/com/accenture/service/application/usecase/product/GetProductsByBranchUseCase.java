package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Listar los productos de una sucursal específica.
 */
public class GetProductsByBranchUseCase {

    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public GetProductsByBranchUseCase(BranchRepositoryPort branchRepositoryPort,
                                      ProductRepositoryPort productRepositoryPort) {
        this.branchRepositoryPort = branchRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    public Flux<Product> execute(Long branchId) {
        if (branchId == null) {
            return Flux.error(new IllegalArgumentException("El ID de la sucursal es obligatorio"));
        }

        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMapMany(branch -> productRepositoryPort.findByBranchId(branch.getId()));
    }
}
