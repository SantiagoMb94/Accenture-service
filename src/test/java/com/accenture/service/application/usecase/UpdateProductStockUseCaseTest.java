package com.accenture.service.application.usecase;

import com.accenture.service.application.usecase.product.UpdateProductStockUseCase;
import com.accenture.service.domain.exception.InvalidStockException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductStockUseCase(productRepositoryPort);
    }

    @Test
    @DisplayName("Debe actualizar el stock de un producto exitosamente")
    void shouldUpdateProductStockSuccessfully() {
        // Arrange
        Long productId = 50L;
        Integer newStock = 120;
        Product existingProduct = new Product(productId, 10L, "Camisa Formal", 30);
        Product updatedProduct = existingProduct.withStock(newStock);

        when(productRepositoryPort.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        // Act
        Mono<Product> result = useCase.execute(productId, newStock);

        // Assert
        StepVerifier.create(result)
                .assertNext(product -> {
                    assertThat(product.getId()).isEqualTo(productId);
                    assertThat(product.getStock()).isEqualTo(120);
                    assertThat(product.getName()).isEqualTo("Camisa Formal");
                })
                .verifyComplete();

        verify(productRepositoryPort, times(1)).findById(productId);
        verify(productRepositoryPort, times(1)).save(argThat(p -> p.getStock().equals(120)));
    }

    @Test
    @DisplayName("Debe lanzar ProductNotFoundException cuando el producto a actualizar no existe")
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;
        when(productRepositoryPort.findById(nonExistentId)).thenReturn(Mono.empty());

        // Act
        Mono<Product> result = useCase.execute(nonExistentId, 10);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        ((ProductNotFoundException) throwable).getProductId().equals(nonExistentId))
                .verify();

        verify(productRepositoryPort, times(1)).findById(nonExistentId);
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException cuando el nuevo stock es negativo")
    void shouldThrowInvalidStockExceptionWhenStockIsNegative() {
        // Act
        Mono<Product> result = useCase.execute(50L, -5);

        // Assert
        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(productRepositoryPort, never()).findById(anyLong());
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidStockException cuando el nuevo stock es nulo")
    void shouldThrowInvalidStockExceptionWhenStockIsNull() {
        // Act
        Mono<Product> result = useCase.execute(50L, null);

        // Assert
        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(productRepositoryPort, never()).findById(anyLong());
        verify(productRepositoryPort, never()).save(any());
    }
}
