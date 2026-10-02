package com.accenture.service.infrastructure.adapter.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * Entidad de Persistencia R2DBC para la tabla 'franchises'.
 */
@Table("franchises")
public class FranchiseEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("created_at")
    private Instant createdAt;

    public FranchiseEntity() {
    }

    public FranchiseEntity(Long id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
