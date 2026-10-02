package com.accenture.service.infrastructure.adapter.r2dbc;

import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.infrastructure.adapter.r2dbc.mapper.BranchEntityMapper;
import com.accenture.service.infrastructure.adapter.r2dbc.repository.BranchR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de Salida (Driven Adapter) para BranchRepositoryPort.
 * Conecta el puerto de dominio con el repositorio reactivo R2DBC.
 */
@Component
public class BranchR2dbcAdapter implements BranchRepositoryPort {

    private final BranchR2dbcRepository repository;

    public BranchR2dbcAdapter(BranchR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return Mono.just(branch)
                .map(BranchEntityMapper::toEntity)
                .flatMap(repository::save)
                .map(BranchEntityMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return repository.findById(id)
                .map(BranchEntityMapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(BranchEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
