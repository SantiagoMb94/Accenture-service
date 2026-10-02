package com.accenture.service.application.usecase.product;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductsByBranchUseCaseTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private GetProductsByBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetProductsByBranchUseCase(branchRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("Debe listar productos de una sucursal existente")
    void shouldListProductsOfBranch() {
        Long branchId = 10L;
        Branch branch = new Branch(branchId, 1L, "Sucursal Norte");
        Product p1 = new Product(101L, branchId, "Café", 15);
        Product p2 = new Product(102L, branchId, "Té", 30);

        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.findByBranchId(branchId)).thenReturn(Flux.just(p1, p2));

        Flux<Product> result = useCase.execute(branchId);

        StepVerifier.create(result)
                .assertNext(product -> assertThat(product.getName()).isEqualTo("Café"))
                .assertNext(product -> assertThat(product.getName()).isEqualTo("Té"))
                .verifyComplete();

        verify(branchRepositoryPort, times(1)).findById(branchId);
        verify(productRepositoryPort, times(1)).findByBranchId(branchId);
    }

    @Test
    @DisplayName("Debe lanzar BranchNotFoundException si la sucursal no existe")
    void shouldThrowNotFoundWhenBranchDoesNotExist() {
        Long branchId = 99L;
        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.empty());

        Flux<Product> result = useCase.execute(branchId);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepositoryPort, times(1)).findById(branchId);
        verify(productRepositoryPort, never()).findByBranchId(anyLong());
    }

    @Test
    @DisplayName("Debe fallar si el ID de sucursal es nulo")
    void shouldFailWhenBranchIdIsNull() {
        Flux<Product> result = useCase.execute(null);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(branchRepositoryPort, never()).findById(anyLong());
    }
}
