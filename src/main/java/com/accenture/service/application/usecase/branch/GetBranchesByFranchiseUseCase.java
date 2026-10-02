package com.accenture.service.application.usecase.branch;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Listar las sucursales pertenecientes a una franquicia.
 */
public class GetBranchesByFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;

    public GetBranchesByFranchiseUseCase(FranchiseRepositoryPort franchiseRepositoryPort,
                                         BranchRepositoryPort branchRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
        this.branchRepositoryPort = branchRepositoryPort;
    }

    public Flux<Branch> execute(Long franchiseId) {
        if (franchiseId == null) {
            return Flux.error(new IllegalArgumentException("El ID de la franquicia es obligatorio"));
        }

        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> branchRepositoryPort.findByFranchiseId(franchise.getId()));
    }
}
