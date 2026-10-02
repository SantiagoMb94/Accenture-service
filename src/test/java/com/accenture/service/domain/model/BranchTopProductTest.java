package com.accenture.service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchTopProductTest {

    @Test
    @DisplayName("Debe manejar BranchTopProduct con producto")
    void shouldHandleBranchTopProductWithProduct() {
        Product product = new Product(100L, 10L, "Café", 80);
        BranchTopProduct model = new BranchTopProduct(10L, "Sucursal Norte", product);

        assertThat(model.getBranchId()).isEqualTo(10L);
        assertThat(model.getBranchName()).isEqualTo("Sucursal Norte");
        assertThat(model.getTopProduct()).isEqualTo(product);
        assertThat(model.hasProduct()).isTrue();
    }

    @Test
    @DisplayName("Debe manejar BranchTopProduct sin producto")
    void shouldHandleBranchTopProductWithoutProduct() {
        BranchTopProduct model = new BranchTopProduct(10L, "Sucursal Vacía", null);

        assertThat(model.getBranchId()).isEqualTo(10L);
        assertThat(model.getBranchName()).isEqualTo("Sucursal Vacía");
        assertThat(model.getTopProduct()).isNull();
        assertThat(model.hasProduct()).isFalse();
    }

    @Test
    @DisplayName("Debe validar equals y hashCode")
    void shouldVerifyEqualsAndHashCode() {
        BranchTopProduct m1 = new BranchTopProduct(10L, "Sucursal Norte", null);
        BranchTopProduct m2 = new BranchTopProduct(10L, "Sucursal Norte", null);

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
        assertThat(m1.toString()).contains("Sucursal Norte");
    }
}
