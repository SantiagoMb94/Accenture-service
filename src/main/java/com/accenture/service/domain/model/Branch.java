package com.accenture.service.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio: Branch (Sucursal).
 * Representa una sucursal física asociada a una franquicia.
 * POJO puro libre de frameworks o dependencias externas.
 */
public class Branch {

    private final Long id;
    private final Long franchiseId;
    private final String name;
    private final Instant createdAt;

    public Branch(Long id, Long franchiseId, String name, Instant createdAt) {
        if (franchiseId == null) {
            throw new IllegalArgumentException("El ID de la franquicia es obligatorio");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la sucursal no puede estar vacío");
        }
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name.trim();
        this.createdAt = createdAt;
    }

    public Branch(Long id, Long franchiseId, String name) {
        this(id, franchiseId, name, Instant.now());
    }

    public Branch(Long franchiseId, String name) {
        this(null, franchiseId, name, Instant.now());
    }

    public Long getId() {
        return id;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Branch withName(String newName) {
        return new Branch(this.id, this.franchiseId, newName, this.createdAt);
    }

    public Branch withId(Long newId) {
        return new Branch(newId, this.franchiseId, this.name, this.createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id) &&
                Objects.equals(franchiseId, branch.franchiseId) &&
                Objects.equals(name, branch.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, franchiseId, name);
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", franchiseId=" + franchiseId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
