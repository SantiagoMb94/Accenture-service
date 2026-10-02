package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Branch;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.BranchEntity;

/**
 * Mapeador explícito entre BranchEntity (R2DBC) y Branch (Dominio).
 */
public final class BranchEntityMapper {

    private BranchEntityMapper() {
    }

    public static Branch toDomain(BranchEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Branch(entity.getId(), entity.getFranchiseId(), entity.getName(), entity.getCreatedAt());
    }

    public static BranchEntity toEntity(Branch domain) {
        if (domain == null) {
            return null;
        }
        return new BranchEntity(domain.getId(), domain.getFranchiseId(), domain.getName(), domain.getCreatedAt());
    }
}
