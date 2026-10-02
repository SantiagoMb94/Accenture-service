package com.accenture.service.infrastructure.adapter.r2dbc;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import com.accenture.service.infrastructure.adapter.r2dbc.mapper.FranchiseEntityMapper;
import com.accenture.service.infrastructure.adapter.r2dbc.repository.FranchiseR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de Salida (Driven Adapter) para FranchiseRepositoryPort.
 * Conecta el puerto de dominio con el repositorio reactivo R2DBC.
 */
@Component
public class FranchiseR2dbcAdapter implements FranchiseRepositoryPort {

    private final FranchiseR2dbcRepository repository;

    public FranchiseR2dbcAdapter(FranchiseR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.just(franchise)
                .map(FranchiseEntityMapper::toEntity)
                .flatMap(repository::save)
                .map(FranchiseEntityMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return repository.findById(id)
                .map(FranchiseEntityMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return repository.findByName(name)
                .map(FranchiseEntityMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(FranchiseEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
