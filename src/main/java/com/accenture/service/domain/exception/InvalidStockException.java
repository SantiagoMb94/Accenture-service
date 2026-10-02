package com.accenture.service.domain.exception;

/**
 * Excepción lanzada cuando se intenta asignar un stock inválido (e.g. negativo o nulo).
 */
public class InvalidStockException extends BusinessRuleException {

    public InvalidStockException(String message) {
        super(message);
    }
}
