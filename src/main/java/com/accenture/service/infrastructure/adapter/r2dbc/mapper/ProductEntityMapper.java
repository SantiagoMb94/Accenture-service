package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Product;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.ProductEntity;

/**
 * Mapeador explícito entre ProductEntity (R2DBC) y Product (Dominio).
 */
public final class ProductEntityMapper {

    private ProductEntityMapper() {
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(
                entity.getId(),
                entity.getBranchId(),
                entity.getName(),
                entity.getStock(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }
        return new ProductEntity(
                domain.getId(),
                domain.getBranchId(),
                domain.getName(),
                domain.getStock(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
