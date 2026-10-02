package com.accenture.service.domain.port.out;

import com.accenture.service.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de Salida: BranchRepositoryPort.
 * Define el contrato de persistencia reactiva para la entidad Branch.
 */
public interface BranchRepositoryPort {

    Mono<Branch> save(Branch branch);

    Mono<Branch> findById(Long id);

    Flux<Branch> findByFranchiseId(Long franchiseId);

    Mono<Boolean> existsById(Long id);
}
