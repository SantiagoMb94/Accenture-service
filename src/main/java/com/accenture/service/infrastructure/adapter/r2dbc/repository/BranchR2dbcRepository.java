package com.accenture.service.infrastructure.adapter.r2dbc.repository;

import com.accenture.service.infrastructure.adapter.r2dbc.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para entidades BranchEntity.
 */
@Repository
public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Long> {

    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

    Mono<BranchEntity> findByFranchiseIdAndName(Long franchiseId, String name);
}
