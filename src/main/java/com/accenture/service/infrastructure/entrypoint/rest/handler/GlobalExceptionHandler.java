package com.accenture.service.infrastructure.entrypoint.rest.handler;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.exception.BusinessRuleException;
import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.exception.InvalidStockException;
import com.accenture.service.domain.exception.ProductNotFoundException;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador Global de Excepciones Reactivo.
 * Transforma excepciones de negocio, validaciones y errores inesperados
 * en respuestas uniformes bajo el estándar RFC 7807 (Problem Details).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            FranchiseNotFoundException.class,
            BranchNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(BusinessRuleException ex, ServerHttpRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/not-found"),
                "Recurso No Encontrado",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidStockException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStockException(InvalidStockException ex, ServerHttpRequest request) {
        log.warn("Stock inválido: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/invalid-stock"),
                "Stock Inválido",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(WebExchangeBindException ex, ServerHttpRequest request) {
        log.warn("Error de validación en request: {}", ex.getMessage());
        Map<String, String> invalidParams = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/validation-error"),
                "Error de Validación de Parámetros",
                HttpStatus.BAD_REQUEST.value(),
                "Uno o más campos enviados en la solicitud no cumplen las reglas de validación",
                request.getPath().value(),
                invalidParams
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException ex, ServerHttpRequest request) {
        log.warn("Entrada malformada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/bad-request"),
                "Petición Inválida",
                HttpStatus.BAD_REQUEST.value(),
                "El cuerpo de la solicitud o los parámetros enviados no tienen el formato esperado",
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, ServerHttpRequest request) {
        log.warn("Violación de integridad de datos: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/conflict"),
                "Conflicto de Integridad de Datos",
                HttpStatus.CONFLICT.value(),
                "El registro no pudo procesarse debido a una restricción de unicidad o clave foránea",
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, ServerHttpRequest request) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/bad-argument"),
                "Argumento Inválido",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, ServerHttpRequest request) {
        log.error("Error interno del servidor no controlado: ", ex);
        ErrorResponse error = new ErrorResponse(
                URI.create("https://api.accenture.com/errors/internal-server-error"),
                "Error Interno del Servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ha ocurrido un error inesperado al procesar la solicitud",
                request.getPath().value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
