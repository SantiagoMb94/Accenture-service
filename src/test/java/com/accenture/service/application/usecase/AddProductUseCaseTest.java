package com.accenture.service.application.usecase;

import com.accenture.service.application.usecase.product.AddProductUseCase;
import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.exception.InvalidStockException;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductUseCaseTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private AddProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddProductUseCase(branchRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("Debe agregar un producto con stock inicial a una sucursal existente exitosamente")
    void shouldAddProductSuccessfully() {
        // Arrange
        Long branchId = 15L;
        String productName = "Café Especial";
        Integer initialStock = 25;

        Branch branch = new Branch(branchId, 1L, "Sucursal Centro");
        Product savedProduct = new Product(100L, branchId, productName, initialStock);

        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        // Act
        Mono<Product> result = useCase.execute(branchId, productName, initialStock);

        // Assert
        StepVerifier.create(result)
                .assertNext(product -> {
                    assertThat(product.getId()).isEqualTo(100L);
                    assertThat(product.getBranchId()).isEqualTo(branchId);
                    assertThat(product.getName()).isEqualTo(productName);
                    assertThat(product.getStock()).isEqualTo(initialStock);
                })
                .verifyComplete();

        verify(branchRepositoryPort, times(1)).findById(branchId);
        verify(productRepositoryPort, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar BranchNotFoundException cuando la sucursal no existe")
    void shouldThrowBranchNotFoundExceptionWhenBranchDoesNotExist() {
        // Arrange
        Long nonExistentBranchId = 999L;
        when(branchRepositoryPort.findById(nonExistentBranchId)).thenReturn(Mono.empty());

        // Act
        Mono<Product> result = useCase.execute(nonExistentBranchId, "Café Especial", 20);

        // Assert
        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepositoryPort, times(1)).findById(nonExistentBranchId);
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el stock inicial es negativo")
    void shouldFailIfStockIsNegative() {
        // Act
        Mono<Product> result = useCase.execute(10L, "Café Especial", -1);

        // Assert
        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(branchRepositoryPort, never()).findById(anyLong());
        verify(productRepositoryPort, never()).save(any());
    }
}
