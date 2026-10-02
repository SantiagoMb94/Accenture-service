package com.accenture.service.domain.exception;

/**
 * Excepción lanzada cuando una franquicia solicitada no existe.
 */
public class FranchiseNotFoundException extends BusinessRuleException {

    private final Long franchiseId;

    public FranchiseNotFoundException(Long franchiseId) {
        super("No se encontró la franquicia con ID: " + franchiseId);
        this.franchiseId = franchiseId;
    }

    public FranchiseNotFoundException(String message) {
        super(message);
        this.franchiseId = null;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }
}
