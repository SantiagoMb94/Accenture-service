package com.accenture.service.infrastructure.entrypoint.rest.dto.response;

import com.accenture.service.domain.model.Branch;

import java.time.Instant;

public class BranchResponse {

    private Long id;
    private Long franchiseId;
    private String name;
    private Instant createdAt;

    public BranchResponse() {
    }

    public BranchResponse(Long id, Long franchiseId, String name, Instant createdAt) {
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static BranchResponse fromDomain(Branch branch) {
        if (branch == null) {
            return null;
        }
        return new BranchResponse(
                branch.getId(),
                branch.getFranchiseId(),
                branch.getName(),
                branch.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
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
