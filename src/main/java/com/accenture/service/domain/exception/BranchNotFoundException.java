package com.accenture.service.domain.exception;

/**
 * Excepción lanzada cuando una sucursal solicitada no existe.
 */
public class BranchNotFoundException extends BusinessRuleException {

    private final Long branchId;

    public BranchNotFoundException(Long branchId) {
        super("No se encontró la sucursal con ID: " + branchId);
        this.branchId = branchId;
    }

    public BranchNotFoundException(String message) {
        super(message);
        this.branchId = null;
    }

    public Long getBranchId() {
        return branchId;
    }
}
