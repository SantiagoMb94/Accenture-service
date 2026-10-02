package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Flux;

/**
 * Caso de Uso: Listar todas las franquicias registradas.
 */
public class GetAllFranchisesUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public GetAllFranchisesUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
    }

    public Flux<Franchise> execute() {
        return franchiseRepositoryPort.findAll();
    }
}
