package com.accenture.service.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio: Franchise (Franquicia).
 * Representa una franquicia comercial dentro del sistema.
 * POJO puro libre de frameworks o dependencias externas.
 */
public class Franchise {

    private final Long id;
    private final String name;
    private final Instant createdAt;

    public Franchise(Long id, String name, Instant createdAt) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la franquicia no puede estar vacío");
        }
        this.id = id;
        this.name = name.trim();
        this.createdAt = createdAt;
    }

    public Franchise(Long id, String name) {
        this(id, name, Instant.now());
    }

    public Franchise(String name) {
        this(null, name, Instant.now());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Franchise withName(String newName) {
        return new Franchise(this.id, newName, this.createdAt);
    }

    public Franchise withId(Long newId) {
        return new Franchise(newId, this.name, this.createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Franchise franchise = (Franchise) o;
        return Objects.equals(id, franchise.id) && Objects.equals(name, franchise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Franchise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
