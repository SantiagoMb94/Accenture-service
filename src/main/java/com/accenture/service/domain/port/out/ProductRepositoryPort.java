package com.accenture.service.domain.port.out;

import com.accenture.service.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de Salida: ProductRepositoryPort.
 * Define el contrato de persistencia reactiva para la entidad Product.
 */
public interface ProductRepositoryPort {

    Mono<Product> save(Product product);

    Mono<Product> findById(Long id);

    Flux<Product> findByBranchId(Long branchId);

    Mono<Void> deleteById(Long id);

    Mono<Boolean> existsById(Long id);
}
