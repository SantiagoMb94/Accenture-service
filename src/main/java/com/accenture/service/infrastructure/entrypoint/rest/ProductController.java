package com.accenture.service.infrastructure.entrypoint.rest;

import com.accenture.service.application.usecase.product.DeleteProductUseCase;
import com.accenture.service.application.usecase.product.UpdateProductNameUseCase;
import com.accenture.service.application.usecase.product.UpdateProductStockUseCase;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.UpdateNameRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.UpdateProductStockRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador REST Reactivo para la gestión de Productos individuales.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(UpdateProductNameUseCase updateProductNameUseCase,
                             UpdateProductStockUseCase updateProductStockUseCase,
                             DeleteProductUseCase deleteProductUseCase) {
        this.updateProductNameUseCase = updateProductNameUseCase;
        this.updateProductStockUseCase = updateProductStockUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PatchMapping("/{id}/name")
    public Mono<ProductResponse> updateProductName(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateNameRequest request) {
        return updateProductNameUseCase.execute(id, request.getName())
                .map(ProductResponse::fromDomain);
    }

    @PatchMapping("/{id}/stock")
    public Mono<ProductResponse> updateProductStock(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateProductStockRequest request) {
        return updateProductStockUseCase.execute(id, request.getStock())
                .map(ProductResponse::fromDomain);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return deleteProductUseCase.execute(id);
    }
}
