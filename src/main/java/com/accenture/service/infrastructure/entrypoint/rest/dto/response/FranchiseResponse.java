package com.accenture.service.infrastructure.entrypoint.rest.dto.response;

import com.accenture.service.domain.model.Franchise;

import java.time.Instant;

public class FranchiseResponse {

    private Long id;
    private String name;
    private Instant createdAt;

    public FranchiseResponse() {
    }

    public FranchiseResponse(Long id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static FranchiseResponse fromDomain(Franchise franchise) {
        if (franchise == null) {
            return null;
        }
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName(),
                franchise.getCreatedAt()
        );
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
