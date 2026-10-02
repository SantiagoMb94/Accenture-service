package com.accenture.service.infrastructure.adapter.r2dbc;

import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import com.accenture.service.infrastructure.adapter.r2dbc.mapper.ProductEntityMapper;
import com.accenture.service.infrastructure.adapter.r2dbc.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de Salida (Driven Adapter) para ProductRepositoryPort.
 * Conecta el puerto de dominio con el repositorio reactivo R2DBC.
 */
@Component
public class ProductR2dbcAdapter implements ProductRepositoryPort {

    private final ProductR2dbcRepository repository;

    public ProductR2dbcAdapter(ProductR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .map(ProductEntityMapper::toEntity)
                .flatMap(repository::save)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repository.findById(id)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return repository.findByBranchId(branchId)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
