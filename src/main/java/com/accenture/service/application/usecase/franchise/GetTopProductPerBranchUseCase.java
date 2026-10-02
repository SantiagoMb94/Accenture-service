package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.BranchTopProduct;
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
 * - flatMap: Concurrencia y combinación asíncrona de flujos por sucursal.
 * - reduce: Operador funcional de agregación reactiva para seleccionar el producto con stock máximo.
 * - defaultIfEmpty: Manejo funcional elegante para sucursales que no tienen productos registrados aún.
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
                .flatMap(branch ->
                        productRepositoryPort.findByBranchId(branch.getId())
                                .reduce((p1, p2) -> p1.getStock() >= p2.getStock() ? p1 : p2)
                                .map(topProduct -> new BranchTopProduct(branch.getId(), branch.getName(), topProduct))
                                .defaultIfEmpty(new BranchTopProduct(branch.getId(), branch.getName(), null))
                );
    }
}
