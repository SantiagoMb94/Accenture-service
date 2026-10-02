package com.accenture.service.infrastructure.entrypoint.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateProductStockRequest {

    @NotNull(message = "El nuevo stock es obligatorio")
    @PositiveOrZero(message = "El nuevo stock debe ser mayor o igual a cero")
    private Integer stock;

    public UpdateProductStockRequest() {
    }

    public UpdateProductStockRequest(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
