package com.accenture.service.infrastructure.entrypoint.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador raíz de bienvenida e índice de la API para navegación rápida.
 */
@RestController
public class RootController {

    @GetMapping("/")
    public Mono<Map<String, Object>> root() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("service", "Franchise Management Reactive Service API");
        response.put("status", "UP");
        response.put("version", "v1");
        response.put("repository", "https://github.com/SantiagoMb94/Accenture-service");
        response.put("health", "/actuator/health");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("franchises", "/api/v1/franchises");
        endpoints.put("branches", "/api/v1/branches");
        endpoints.put("products", "/api/v1/products");
        endpoints.put("topStockByFranchise", "/api/v1/franchises/{id}/max-stock-products");

        response.put("endpoints", endpoints);
        return Mono.just(response);
    }
}
