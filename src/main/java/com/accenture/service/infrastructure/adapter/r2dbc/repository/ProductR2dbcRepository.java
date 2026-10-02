package com.accenture.service.infrastructure.adapter.r2dbc.repository;

import com.accenture.service.infrastructure.adapter.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para entidades ProductEntity.
 */
@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    @Query("SELECT * FROM products WHERE branch_id = :branchId ORDER BY stock DESC, id ASC")
    Flux<ProductEntity> findByBranchId(Long branchId);

    Mono<ProductEntity> findByBranchIdAndName(Long branchId, String name);
}
