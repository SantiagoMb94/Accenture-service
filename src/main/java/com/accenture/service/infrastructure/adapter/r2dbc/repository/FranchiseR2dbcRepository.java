package com.accenture.service.infrastructure.adapter.r2dbc.repository;

import com.accenture.service.infrastructure.adapter.r2dbc.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para entidades FranchiseEntity.
 */
@Repository
public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {

    Mono<FranchiseEntity> findByName(String name);
}
