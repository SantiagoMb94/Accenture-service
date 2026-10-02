package com.accenture.service.infrastructure.adapter.r2dbc.mapper;

import com.accenture.service.domain.model.Product;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityMapperTest {

    @Test
    @DisplayName("Debe mapear de ProductEntity a Product correctamente")
    void shouldMapEntityToDomain() {
        Instant now = Instant.now();
        ProductEntity entity = new ProductEntity(100L, 10L, "Café", 40, now, now);

        Product domain = ProductEntityMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(100L);
        assertThat(domain.getBranchId()).isEqualTo(10L);
        assertThat(domain.getName()).isEqualTo("Café");
        assertThat(domain.getStock()).isEqualTo(40);
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe mapear de Product a ProductEntity correctamente")
    void shouldMapDomainToEntity() {
        Instant now = Instant.now();
        Product domain = new Product(100L, 10L, "Café", 40, now, now);

        ProductEntity entity = ProductEntityMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(100L);
        assertThat(entity.getBranchId()).isEqualTo(10L);
        assertThat(entity.getName()).isEqualTo("Café");
        assertThat(entity.getStock()).isEqualTo(40);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe retornar null si la entrada es nula")
    void shouldReturnNullWhenInputIsNull() {
        assertThat(ProductEntityMapper.toDomain(null)).isNull();
        assertThat(ProductEntityMapper.toEntity(null)).isNull();
    }
}
