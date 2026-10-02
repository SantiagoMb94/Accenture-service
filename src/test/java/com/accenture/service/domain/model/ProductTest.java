package com.accenture.service.domain.model;

import com.accenture.service.domain.exception.InvalidStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("Debe crear una instancia válida de Product")
    void shouldCreateValidProduct() {
        Instant now = Instant.now();
        Product product = new Product(100L, 10L, "Café", 50, now, now);

        assertThat(product.getId()).isEqualTo(100L);
        assertThat(product.getBranchId()).isEqualTo(10L);
        assertThat(product.getName()).isEqualTo("Café");
        assertThat(product.getStock()).isEqualTo(50);
        assertThat(product.getCreatedAt()).isEqualTo(now);
        assertThat(product.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el branchId es nulo")
    void shouldThrowExceptionWhenBranchIdIsNull() {
        assertThatThrownBy(() -> new Product(100L, null, "Café", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El ID de la sucursal es obligatorio");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre es nulo o vacío")
    void shouldThrowExceptionWhenNameIsInvalid() {
        assertThatThrownBy(() -> new Product(100L, 10L, "  ", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre del producto no puede estar vacío");
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException si el stock es negativo o nulo")
    void shouldThrowExceptionWhenStockIsInvalid() {
        assertThatThrownBy(() -> new Product(100L, 10L, "Café", -5))
                .isInstanceOf(InvalidStockException.class)
                .hasMessageContaining("El stock no puede ser nulo ni negativo");

        assertThatThrownBy(() -> new Product(100L, 10L, "Café", null))
                .isInstanceOf(InvalidStockException.class);
    }

    @Test
    @DisplayName("Debe actualizar el nombre correctamente con withName")
    void shouldUpdateNameWithWithName() {
        Product product = new Product(100L, 10L, "Café", 50);
        Product updated = product.withName("Té Verde");

        assertThat(updated.getName()).isEqualTo("Té Verde");
        assertThat(updated.getStock()).isEqualTo(50);
        assertThat(updated.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nuevo nombre con withName es inválido")
    void shouldThrowExceptionWhenUpdatingWithInvalidName() {
        Product product = new Product(100L, 10L, "Café", 50);
        assertThatThrownBy(() -> product.withName("  "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Debe actualizar el stock correctamente con withStock")
    void shouldUpdateStockWithWithStock() {
        Product product = new Product(100L, 10L, "Café", 50);
        Product updated = product.withStock(120);

        assertThat(updated.getStock()).isEqualTo(120);
        assertThat(updated.getName()).isEqualTo("Café");
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException si el nuevo stock con withStock es negativo")
    void shouldThrowExceptionWhenUpdatingWithNegativeStock() {
        Product product = new Product(100L, 10L, "Café", 50);
        assertThatThrownBy(() -> product.withStock(-1))
                .isInstanceOf(InvalidStockException.class);
    }

    @Test
    @DisplayName("Debe validar equals y hashCode")
    void shouldVerifyEqualsAndHashCode() {
        Product p1 = new Product(100L, 10L, "Café", 50);
        Product p2 = new Product(100L, 10L, "Café", 50);

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        assertThat(p1.toString()).contains("Café");
    }
}
