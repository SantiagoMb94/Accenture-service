package com.accenture.service.application.usecase;

import com.accenture.service.application.usecase.franchise.GetTopProductPerBranchUseCase;
import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.model.BranchTopProduct;
import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTopProductPerBranchUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private GetTopProductPerBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTopProductPerBranchUseCase(franchiseRepositoryPort, branchRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("Debe retornar el producto con mayor stock por cada sucursal de manera no bloqueante")
    void shouldReturnTopProductPerBranchSuccessfully() {
        // Arrange
        Long franchiseId = 1L;
        Franchise franchise = new Franchise(franchiseId, "Franquicia Éxito", Instant.now());

        Branch branch1 = new Branch(10L, franchiseId, "Sucursal Norte", Instant.now());
        Branch branch2 = new Branch(20L, franchiseId, "Sucursal Sur", Instant.now());

        Product prod1A = new Product(101L, 10L, "Camiseta", 15);
        Product prod1B = new Product(102L, 10L, "Pantalón", 45); // Top en Norte
        Product prod1C = new Product(103L, 10L, "Chaqueta", 20);

        Product prod2A = new Product(201L, 20L, "Zapatos", 80); // Top en Sur
        Product prod2B = new Product(202L, 20L, "Gorra", 30);

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch1, branch2));
        when(productRepositoryPort.findByBranchId(10L)).thenReturn(Flux.just(prod1A, prod1B, prod1C));
        when(productRepositoryPort.findByBranchId(20L)).thenReturn(Flux.just(prod2A, prod2B));

        // Act
        Flux<BranchTopProduct> resultFlux = useCase.execute(franchiseId);

        // Assert con StepVerifier reactivo
        StepVerifier.create(resultFlux)
                .assertNext(topBranch -> {
                    assertThat(topBranch.getBranchId()).isEqualTo(10L);
                    assertThat(topBranch.getBranchName()).isEqualTo("Sucursal Norte");
                    assertThat(topBranch.hasProduct()).isTrue();
                    assertThat(topBranch.getTopProduct().getName()).isEqualTo("Pantalón");
                    assertThat(topBranch.getTopProduct().getStock()).isEqualTo(45);
                })
                .assertNext(topBranch -> {
                    assertThat(topBranch.getBranchId()).isEqualTo(20L);
                    assertThat(topBranch.getBranchName()).isEqualTo("Sucursal Sur");
                    assertThat(topBranch.hasProduct()).isTrue();
                    assertThat(topBranch.getTopProduct().getName()).isEqualTo("Zapatos");
                    assertThat(topBranch.getTopProduct().getStock()).isEqualTo(80);
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, times(1)).findByFranchiseId(franchiseId);
        verify(productRepositoryPort, times(1)).findByBranchId(10L);
        verify(productRepositoryPort, times(1)).findByBranchId(20L);
    }

    @Test
    @DisplayName("Debe manejar graciosamente una sucursal sin productos registrados retornando topProduct nulo")
    void shouldHandleBranchWithoutProductsGracefully() {
        // Arrange
        Long franchiseId = 1L;
        Franchise franchise = new Franchise(franchiseId, "Franquicia Nueva", Instant.now());
        Branch branchEmpty = new Branch(30L, franchiseId, "Sucursal Vacía", Instant.now());

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branchEmpty));
        when(productRepositoryPort.findByBranchId(30L)).thenReturn(Flux.empty());

        // Act
        Flux<BranchTopProduct> resultFlux = useCase.execute(franchiseId);

        // Assert
        StepVerifier.create(resultFlux)
                .assertNext(topBranch -> {
                    assertThat(topBranch.getBranchId()).isEqualTo(30L);
                    assertThat(topBranch.getBranchName()).isEqualTo("Sucursal Vacía");
                    assertThat(topBranch.hasProduct()).isFalse();
                    assertThat(topBranch.getTopProduct()).isNull();
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, times(1)).findByFranchiseId(franchiseId);
        verify(productRepositoryPort, times(1)).findByBranchId(30L);
    }

    @Test
    @DisplayName("Debe emitir FranchiseNotFoundException cuando la franquicia no existe")
    void shouldThrowFranchiseNotFoundExceptionWhenFranchiseDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;
        when(franchiseRepositoryPort.findById(nonExistentId)).thenReturn(Mono.empty());

        // Act
        Flux<BranchTopProduct> resultFlux = useCase.execute(nonExistentId);

        // Assert
        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof FranchiseNotFoundException &&
                        ((FranchiseNotFoundException) throwable).getFranchiseId().equals(nonExistentId))
                .verify();

        verify(franchiseRepositoryPort, times(1)).findById(nonExistentId);
        verify(branchRepositoryPort, never()).findByFranchiseId(anyLong());
        verify(productRepositoryPort, never()).findByBranchId(anyLong());
    }
}
