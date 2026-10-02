package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Obtener una franquicia por su ID.
 */
public class GetFranchiseByIdUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public GetFranchiseByIdUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
    }

    public Mono<Franchise> execute(Long id) {
        return franchiseRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)));
    }
}
