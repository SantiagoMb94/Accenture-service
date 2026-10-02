package com.accenture.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceApplicationTests {

    @Test
    @DisplayName("Verificación básica del entorno de pruebas unitarias")
    void testEnvironmentSmokeTest() {
        assertThat(ServiceApplication.class).isNotNull();
    }
}
