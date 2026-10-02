package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Branch;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.BranchEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class BranchEntityMapperTest {

    @Test
    @DisplayName("Debe mapear de BranchEntity a Branch correctamente")
    void shouldMapEntityToDomain() {
        Instant now = Instant.now();
        BranchEntity entity = new BranchEntity(10L, 1L, "Sucursal Norte", now);

        Branch domain = BranchEntityMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(10L);
        assertThat(domain.getFranchiseId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("Sucursal Norte");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe mapear de Branch a BranchEntity correctamente")
    void shouldMapDomainToEntity() {
        Instant now = Instant.now();
        Branch domain = new Branch(10L, 1L, "Sucursal Norte", now);

        BranchEntity entity = BranchEntityMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getFranchiseId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Sucursal Norte");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe retornar null si la entrada es nula")
    void shouldReturnNullWhenInputIsNull() {
        assertThat(BranchEntityMapper.toDomain(null)).isNull();
        assertThat(BranchEntityMapper.toEntity(null)).isNull();
    }
}
