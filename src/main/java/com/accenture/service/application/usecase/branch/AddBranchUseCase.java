package com.accenture.service.application.usecase.branch;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Agregar una nueva sucursal a una franquicia específica.
 */
public class AddBranchUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;

    public AddBranchUseCase(FranchiseRepositoryPort franchiseRepositoryPort,
                            BranchRepositoryPort branchRepositoryPort) {
        this.franchiseRepositoryPort = franchiseRepositoryPort;
        this.branchRepositoryPort = branchRepositoryPort;
    }

    public Mono<Branch> execute(Long franchiseId, String branchName) {
        if (franchiseId == null) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia es obligatorio"));
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío"));
        }

        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> new Branch(franchise.getId(), branchName.trim()))
                .flatMap(branchRepositoryPort::save);
    }
}
