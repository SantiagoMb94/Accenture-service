package com.accenture.service.infrastructure.entrypoint.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

/**
 * Representación de errores conforme al estándar RFC 7807 (Problem Details for HTTP APIs).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private URI type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Instant timestamp;
    private Map<String, String> invalidParams;

    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(URI type, String title, int status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.timestamp = Instant.now();
    }

    public ErrorResponse(URI type, String title, int status, String detail, String instance, Map<String, String> invalidParams) {
        this(type, title, status, detail, instance);
        this.invalidParams = invalidParams;
    }

    public URI getType() {
        return type;
    }

    public void setType(URI type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(Map<String, String> invalidParams) {
        this.invalidParams = invalidParams;
    }
}
