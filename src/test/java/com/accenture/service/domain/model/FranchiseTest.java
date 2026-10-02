package com.accenture.service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FranchiseTest {

    @Test
    @DisplayName("Debe crear una instancia válida de Franchise")
    void shouldCreateValidFranchise() {
        Instant now = Instant.now();
        Franchise franchise = new Franchise(1L, "Franquicia A", now);

        assertThat(franchise.getId()).isEqualTo(1L);
        assertThat(franchise.getName()).isEqualTo("Franquicia A");
        assertThat(franchise.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre es nulo o vacío")
    void shouldThrowExceptionWhenNameIsInvalid() {
        assertThatThrownBy(() -> new Franchise(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre de la franquicia no puede estar vacío");

        assertThatThrownBy(() -> new Franchise("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Debe crear una nueva instancia con nuevo nombre usando withName")
    void shouldCreateNewInstanceWithName() {
        Franchise franchise = new Franchise(1L, "Original");
        Franchise updated = franchise.withName("Modificado");

        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getName()).isEqualTo("Modificado");
        assertThat(franchise.getName()).isEqualTo("Original");
    }

    @Test
    @DisplayName("Debe verificar igualdad con equals y hashCode")
    void shouldVerifyEqualsAndHashCode() {
        Instant now = Instant.now();
        Franchise f1 = new Franchise(1L, "Test", now);
        Franchise f2 = new Franchise(1L, "Test", now);

        assertThat(f1).isEqualTo(f2);
        assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
        assertThat(f1.toString()).contains("Test");
    }
}
