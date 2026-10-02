package com.accenture.service.application.usecase;

import com.accenture.service.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
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
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateFranchiseUseCase(franchiseRepositoryPort);
    }

    @Test
    @DisplayName("Debe crear una franquicia correctamente")
    void shouldCreateFranchiseSuccessfully() {
        // Arrange
        String franchiseName = "Franquicia Starbucks";
        Franchise savedFranchise = new Franchise(1L, franchiseName);

        when(franchiseRepositoryPort.save(any(Franchise.class))).thenReturn(Mono.just(savedFranchise));

        // Act
        Mono<Franchise> result = useCase.execute(franchiseName);

        // Assert
        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertThat(franchise.getId()).isEqualTo(1L);
                    assertThat(franchise.getName()).isEqualTo(franchiseName);
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe fallar al intentar crear una franquicia con nombre vacío")
    void shouldFailWhenNameIsEmpty() {
        // Act
        Mono<Franchise> result = useCase.execute("   ");

        // Assert
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(franchiseRepositoryPort, never()).save(any());
    }
}
