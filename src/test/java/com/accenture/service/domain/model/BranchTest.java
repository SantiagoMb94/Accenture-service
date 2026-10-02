package com.accenture.service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BranchTest {

    @Test
    @DisplayName("Debe crear una instancia válida de Branch")
    void shouldCreateValidBranch() {
        Instant now = Instant.now();
        Branch branch = new Branch(10L, 1L, "Sucursal Norte", now);

        assertThat(branch.getId()).isEqualTo(10L);
        assertThat(branch.getFranchiseId()).isEqualTo(1L);
        assertThat(branch.getName()).isEqualTo("Sucursal Norte");
        assertThat(branch.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID de franquicia es nulo")
    void shouldThrowExceptionWhenFranchiseIdIsNull() {
        assertThatThrownBy(() -> new Branch(10L, null, "Sucursal Norte", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El ID de la franquicia es obligatorio");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre es nulo o vacío")
    void shouldThrowExceptionWhenNameIsInvalid() {
        assertThatThrownBy(() -> new Branch(10L, 1L, "   ", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre de la sucursal no puede estar vacío");
    }

    @Test
    @DisplayName("Debe retornar una nueva instancia con withName")
    void shouldUpdateNameWithWithName() {
        Branch branch = new Branch(10L, 1L, "Norte");
        Branch updated = branch.withName("Sur");

        assertThat(updated.getName()).isEqualTo("Sur");
        assertThat(updated.getId()).isEqualTo(10L);
        assertThat(updated.getFranchiseId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debe validar equals y hashCode")
    void shouldVerifyEqualsAndHashCode() {
        Branch b1 = new Branch(10L, 1L, "Norte");
        Branch b2 = new Branch(10L, 1L, "Norte");

        assertThat(b1).isEqualTo(b2);
        assertThat(b1.hashCode()).isEqualTo(b2.hashCode());
        assertThat(b1.toString()).contains("Norte");
    }
}
