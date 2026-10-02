package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.FranchiseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseEntityMapperTest {

    @Test
    @DisplayName("Debe mapear de Entity a Domain correctamente")
    void shouldMapEntityToDomain() {
        Instant now = Instant.now();
        FranchiseEntity entity = new FranchiseEntity(1L, "Franquicia A", now);

        Franchise domain = FranchiseEntityMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("Franquicia A");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe mapear de Domain a Entity correctamente")
    void shouldMapDomainToEntity() {
        Instant now = Instant.now();
        Franchise domain = new Franchise(1L, "Franquicia A", now);

        FranchiseEntity entity = FranchiseEntityMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Franquicia A");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe retornar null si la entrada es nula")
    void shouldReturnNullWhenInputIsNull() {
        assertThat(FranchiseEntityMapper.toDomain(null)).isNull();
        assertThat(FranchiseEntityMapper.toEntity(null)).isNull();
    }
}
