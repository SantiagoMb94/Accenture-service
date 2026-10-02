package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Modificar el nombre de una franquicia existente.
 */
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public UpdateFranchiseNameUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
    }

    public Mono<Franchise> execute(Long franchiseId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nuevo nombre de la franquicia no puede estar vacío"));
        }

        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> franchise.withName(newName.trim()))
                .flatMap(franchiseRepositoryPort::save);
    }
}
