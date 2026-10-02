package com.accenture.service.infrastructure.entrypoint.rest;

import com.accenture.service.application.usecase.branch.UpdateBranchNameUseCase;
import com.accenture.service.application.usecase.product.AddProductUseCase;
import com.accenture.service.application.usecase.product.GetProductsByBranchUseCase;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.CreateProductRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.UpdateNameRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.BranchResponse;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST Reactivo para la gestión de Sucursales y sus Productos.
 */
@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final AddProductUseCase addProductUseCase;
    private final GetProductsByBranchUseCase getProductsByBranchUseCase;

    public BranchController(UpdateBranchNameUseCase updateBranchNameUseCase,
                            AddProductUseCase addProductUseCase,
                            GetProductsByBranchUseCase getProductsByBranchUseCase) {
        this.updateBranchNameUseCase = updateBranchNameUseCase;
        this.addProductUseCase = addProductUseCase;
        this.getProductsByBranchUseCase = getProductsByBranchUseCase;
    }

    @PatchMapping("/{id}/name")
    public Mono<BranchResponse> updateBranchName(@PathVariable Long id,
                                                @Valid @RequestBody UpdateNameRequest request) {
        return updateBranchNameUseCase.execute(id, request.getName())
                .map(BranchResponse::fromDomain);
    }

    @PostMapping("/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProductToBranch(@PathVariable Long branchId,
                                                   @Valid @RequestBody CreateProductRequest request) {
        return addProductUseCase.execute(branchId, request.getName(), request.getStock())
                .map(ProductResponse::fromDomain);
    }

    @GetMapping("/{branchId}/products")
    public Flux<ProductResponse> getProductsByBranch(@PathVariable Long branchId) {
        return getProductsByBranchUseCase.execute(branchId)
                .map(ProductResponse::fromDomain);
    }
}
