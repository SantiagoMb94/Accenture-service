package com.accenture.service.domain.port.out;

import com.accenture.service.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de Salida: FranchiseRepositoryPort.
 * Define el contrato de persistencia reactiva para la entidad Franchise.
 */
public interface FranchiseRepositoryPort {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(Long id);

    Mono<Franchise> findByName(String name);

    Flux<Franchise> findAll();

    Mono<Boolean> existsById(Long id);
}
