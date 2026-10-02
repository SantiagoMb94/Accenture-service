package com.accenture.service.application.usecase;

import com.accenture.service.application.usecase.product.DeleteProductUseCase;
import com.accenture.service.domain.exception.ProductNotFoundException;
import com.accenture.service.domain.model.Product;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private DeleteProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteProductUseCase(productRepositoryPort);
    }

    @Test
    @DisplayName("Debe eliminar un producto exitosamente cuando existe")
    void shouldDeleteProductSuccessfully() {
        // Arrange
        Long productId = 42L;
        Product existingProduct = new Product(productId, 1L, "Laptop Gamer", 10);

        when(productRepositoryPort.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepositoryPort.deleteById(productId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = useCase.execute(productId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepositoryPort, times(1)).findById(productId);
        verify(productRepositoryPort, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Debe lanzar ProductNotFoundException al intentar eliminar un producto inexistente")
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;
        when(productRepositoryPort.findById(nonExistentId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = useCase.execute(nonExistentId);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepositoryPort, times(1)).findById(nonExistentId);
        verify(productRepositoryPort, never()).deleteById(anyLong());
    }
}
