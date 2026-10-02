package com.accenture.service.infrastructure.entrypoint.rest;

import com.accenture.service.application.usecase.branch.AddBranchUseCase;
import com.accenture.service.application.usecase.branch.GetBranchesByFranchiseUseCase;
import com.accenture.service.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.service.application.usecase.franchise.GetAllFranchisesUseCase;
import com.accenture.service.application.usecase.franchise.GetFranchiseByIdUseCase;
import com.accenture.service.application.usecase.franchise.GetTopProductPerBranchUseCase;
import com.accenture.service.application.usecase.franchise.UpdateFranchiseNameUseCase;
import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.BranchTopProduct;
import com.accenture.service.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreateFranchiseUseCase createFranchiseUseCase;

    @MockBean
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @MockBean
    private GetAllFranchisesUseCase getAllFranchisesUseCase;

    @MockBean
    private GetFranchiseByIdUseCase getFranchiseByIdUseCase;

    @MockBean
    private GetTopProductPerBranchUseCase getTopProductPerBranchUseCase;

    @MockBean
    private AddBranchUseCase addBranchUseCase;

    @MockBean
    private GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;

    @Test
    @DisplayName("GET max-stock-products retorna el producto de mayor stock por sucursal")
    void shouldReturnTopProductPerBranch() {
        Product topNorte = new Product(102L, 10L, "Pantalón", 45);
        Product topSur = new Product(201L, 20L, "Zapatos", 80);

        when(getTopProductPerBranchUseCase.execute(1L)).thenReturn(Flux.just(
                new BranchTopProduct(10L, "Sucursal Norte", topNorte),
                new BranchTopProduct(20L, "Sucursal Sur", topSur)
        ));

        webTestClient.get()
                .uri("/api/v1/franchises/{franchiseId}/max-stock-products", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].branchId").isEqualTo(10)
                .jsonPath("$[0].branchName").isEqualTo("Sucursal Norte")
                .jsonPath("$[0].topProduct.id").isEqualTo(102)
                .jsonPath("$[0].topProduct.name").isEqualTo("Pantalón")
                .jsonPath("$[0].topProduct.stock").isEqualTo(45)
                .jsonPath("$[1].branchId").isEqualTo(20)
                .jsonPath("$[1].branchName").isEqualTo("Sucursal Sur")
                .jsonPath("$[1].topProduct.name").isEqualTo("Zapatos")
                .jsonPath("$[1].topProduct.stock").isEqualTo(80);
    }

    @Test
    @DisplayName("GET max-stock-products responde 404 cuando la franquicia no existe")
    void shouldReturnNotFoundWhenFranchiseDoesNotExist() {
        when(getTopProductPerBranchUseCase.execute(999L))
                .thenReturn(Flux.error(new FranchiseNotFoundException(999L)));

        webTestClient.get()
                .uri("/api/v1/franchises/{franchiseId}/max-stock-products", 999)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.title").isEqualTo("Recurso No Encontrado");
    }

    @Test
    @DisplayName("GET max-stock-products incluye la sucursal sin productos con topProduct nulo")
    void shouldReturnNullTopProductWhenBranchHasNoProducts() {
        when(getTopProductPerBranchUseCase.execute(1L)).thenReturn(Flux.just(
                new BranchTopProduct(30L, "Sucursal Vacía", null)
        ));

        webTestClient.get()
                .uri("/api/v1/franchises/{franchiseId}/max-stock-products", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].branchId").isEqualTo(30)
                .jsonPath("$[0].branchName").isEqualTo("Sucursal Vacía")
                .jsonPath("$[0].topProduct").isEmpty();
    }

    @Test
    @DisplayName("POST franquicia con nombre duplicado responde 409 y no 500")
    void shouldReturnConflictWhenFranchiseNameIsDuplicated() {
        when(createFranchiseUseCase.execute("Franquicia Duplicada"))
                .thenReturn(Mono.error(new DataIntegrityViolationException(
                        "duplicate key value violates unique constraint \"franchises_name_key\"")));

        webTestClient.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Franquicia Duplicada\"}")
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.status").isEqualTo(409)
                .jsonPath("$.title").isEqualTo("Conflicto de Integridad de Datos");
    }
}
