package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.BranchTopProduct;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso Analítico Reactivo (Requisito Clave):
 * Consultar el producto con mayor stock por cada sucursal de una franquicia específica.
 *
 * Se resuelve de forma 100% no bloqueante empleando operadores funcionales de Project Reactor:
 * - concatMap: Recorre las sucursales conservando el orden en que fueron consultadas.
 * - reduce: Elige el mayor stock; si empatan, gana el producto con menor id.
 * - defaultIfEmpty: Las sucursales sin productos se mantienen con topProduct nulo.
 */
public class GetTopProductPerBranchUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public GetTopProductPerBranchUseCase(FranchiseRepositoryPort franchiseRepositoryPort,
                                         BranchRepositoryPort branchRepositoryPort,
                                         ProductRepositoryPort productRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
        this.branchRepositoryPort = branchRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    public Flux<BranchTopProduct> execute(Long franchiseId) {
        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> branchRepositoryPort.findByFranchiseId(franchise.getId()))
                .concatMap(branch ->
                        productRepositoryPort.findByBranchId(branch.getId())
                                .reduce(this::productWithMoreStock)
                                .map(topProduct -> new BranchTopProduct(branch.getId(), branch.getName(), topProduct))
                                .defaultIfEmpty(new BranchTopProduct(branch.getId(), branch.getName(), null))
                );
    }

    private Product productWithMoreStock(Product current, Product candidate) {
        int stockComparison = Integer.compare(candidate.getStock(), current.getStock());
        if (stockComparison > 0) {
            return candidate;
        }
        if (stockComparison < 0) {
            return current;
        }
        return candidate.getId() < current.getId() ? candidate : current;
    }
}
