package com.accenture.service.infrastructure.entrypoint.rest;

import com.accenture.service.application.usecase.branch.AddBranchUseCase;
import com.accenture.service.application.usecase.branch.GetBranchesByFranchiseUseCase;
import com.accenture.service.application.usecase.franchise.*;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.CreateBranchRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.CreateFranchiseRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.request.UpdateNameRequest;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.BranchResponse;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.BranchTopProductResponse;
import com.accenture.service.infrastructure.entrypoint.rest.dto.response.FranchiseResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST Reactivo para la gestión de Franquicias y Consultas Analíticas.
 */
@RestController
@RequestMapping("/api/v1/franchises")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final GetAllFranchisesUseCase getAllFranchisesUseCase;
    private final GetFranchiseByIdUseCase getFranchiseByIdUseCase;
    private final GetTopProductPerBranchUseCase getTopProductPerBranchUseCase;
    private final AddBranchUseCase addBranchUseCase;
    private final GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;

    public FranchiseController(CreateFranchiseUseCase createFranchiseUseCase,
                               UpdateFranchiseNameUseCase updateFranchiseNameUseCase,
                               GetAllFranchisesUseCase getAllFranchisesUseCase,
                               GetFranchiseByIdUseCase getFranchiseByIdUseCase,
                               GetTopProductPerBranchUseCase getTopProductPerBranchUseCase,
                               AddBranchUseCase addBranchUseCase,
                               GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
        this.getAllFranchisesUseCase = getAllFranchisesUseCase;
        this.getFranchiseByIdUseCase = getFranchiseByIdUseCase;
        this.getTopProductPerBranchUseCase = getTopProductPerBranchUseCase;
        this.addBranchUseCase = addBranchUseCase;
        this.getBranchesByFranchiseUseCase = getBranchesByFranchiseUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        return createFranchiseUseCase.execute(request.getName())
                .map(FranchiseResponse::fromDomain);
    }

    @GetMapping
    public Flux<FranchiseResponse> getAllFranchises() {
        return getAllFranchisesUseCase.execute()
                .map(FranchiseResponse::fromDomain);
    }

    @GetMapping("/{id}")
    public Mono<FranchiseResponse> getFranchiseById(@PathVariable Long id) {
        return getFranchiseByIdUseCase.execute(id)
                .map(FranchiseResponse::fromDomain);
    }

    @PatchMapping("/{id}/name")
    public Mono<FranchiseResponse> updateFranchiseName(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateNameRequest request) {
        return updateFranchiseNameUseCase.execute(id, request.getName())
                .map(FranchiseResponse::fromDomain);
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> addBranchToFranchise(@PathVariable Long franchiseId,
                                                    @Valid @RequestBody CreateBranchRequest request) {
        return addBranchUseCase.execute(franchiseId, request.getName())
                .map(BranchResponse::fromDomain);
    }

    @GetMapping("/{franchiseId}/branches")
    public Flux<BranchResponse> getBranchesByFranchise(@PathVariable Long franchiseId) {
        return getBranchesByFranchiseUseCase.execute(franchiseId)
                .map(BranchResponse::fromDomain);
    }

    /**
     * Consulta Analítica Reactiva:
     * Retorna el producto con mayor stock por cada sucursal de una franquicia puntual.
     */
    @GetMapping("/{franchiseId}/max-stock-products")
    public Flux<BranchTopProductResponse> getTopProductsPerBranch(@PathVariable Long franchiseId) {
        return getTopProductPerBranchUseCase.execute(franchiseId)
                .map(BranchTopProductResponse::fromDomain);
    }
}
