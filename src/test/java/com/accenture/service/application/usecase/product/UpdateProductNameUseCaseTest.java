package com.accenture.service.application.usecase.product;

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
class UpdateProductNameUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private UpdateProductNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductNameUseCase(productRepositoryPort);
    }

    @Test
    @DisplayName("Debe actualizar el nombre de un producto exitosamente")
    void shouldUpdateProductNameSuccessfully() {
        Long productId = 50L;
        String newName = "Café Orgánico";
        Product existing = new Product(productId, 10L, "Café Regular", 20);
        Product updated = existing.withName(newName);

        when(productRepositoryPort.findById(productId)).thenReturn(Mono.just(existing));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(updated));

        Mono<Product> result = useCase.execute(productId, newName);

        StepVerifier.create(result)
                .assertNext(product -> {
                    assertThat(product.getId()).isEqualTo(productId);
                    assertThat(product.getName()).isEqualTo(newName);
                })
                .verifyComplete();

        verify(productRepositoryPort, times(1)).findById(productId);
        verify(productRepositoryPort, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar ProductNotFoundException si el producto no existe")
    void shouldThrowNotFoundWhenProductDoesNotExist() {
        Long productId = 99L;
        when(productRepositoryPort.findById(productId)).thenReturn(Mono.empty());

        Mono<Product> result = useCase.execute(productId, "Nuevo");

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepositoryPort, times(1)).findById(productId);
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el nuevo nombre es nulo o vacío")
    void shouldFailWhenNameIsInvalid() {
        Mono<Product> result = useCase.execute(50L, "   ");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(productRepositoryPort, never()).findById(anyLong());
        verify(productRepositoryPort, never()).save(any());
    }
}
