package com.accenture.service.application.usecase.branch;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Modificar el nombre de una sucursal existente.
 */
public class UpdateBranchNameUseCase {

    private final BranchRepositoryPort branchRepositoryPort;

    public UpdateBranchNameUseCase(BranchRepositoryPort branchRepositoryPort) {
        this.branchRepositoryPort = branchRepositoryPort;
    }

    public Mono<Branch> execute(Long branchId, String newName) {
        if (branchId == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal es obligatorio"));
        }
        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nuevo nombre de la sucursal no puede estar vacío"));
        }

        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> branch.withName(newName.trim()))
                .flatMap(branchRepositoryPort::save);
    }
}
