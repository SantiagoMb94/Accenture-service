package com.accenture.service.domain.exception;

/**
 * Excepción base para violaciones de reglas de negocio en el dominio.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
