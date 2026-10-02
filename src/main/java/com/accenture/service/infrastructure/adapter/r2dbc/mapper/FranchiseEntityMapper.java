package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.FranchiseEntity;

/**
 * Mapeador explícito entre FranchiseEntity (R2DBC) y Franchise (Dominio).
 */
public final class FranchiseEntityMapper {

    private FranchiseEntityMapper() {
    }

    public static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Franchise(entity.getId(), entity.getName(), entity.getCreatedAt());
    }

    public static FranchiseEntity toEntity(Franchise domain) {
        if (domain == null) {
            return null;
        }
        return new FranchiseEntity(domain.getId(), domain.getName(), domain.getCreatedAt());
    }
}
