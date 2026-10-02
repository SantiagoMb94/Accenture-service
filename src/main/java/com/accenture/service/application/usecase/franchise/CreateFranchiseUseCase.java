package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Crear una nueva Franquicia.
 * Responsabilidad única: Registrar una entidad Franchise en el sistema.
 */
public class CreateFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public CreateFranchiseUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
    }

    public Mono<Franchise> execute(String name) {
        return Mono.justOrEmpty(name)
                .map(String::trim)
                .filter(trimmed -> !trimmed.isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre de la franquicia no puede estar vacío")))
                .map(Franchise::new)
                .flatMap(franchiseRepositoryPort::save);
    }
}
