package com.accenture.service.application.usecase.franchise;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
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
class UpdateFranchiseNameUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    private UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateFranchiseNameUseCase(franchiseRepositoryPort);
    }

    @Test
    @DisplayName("Debe actualizar el nombre de una franquicia exitosamente")
    void shouldUpdateFranchiseNameSuccessfully() {
        Long franchiseId = 1L;
        String newName = "Franquicia Renovada";
        Franchise existing = new Franchise(franchiseId, "Franquicia Vieja");
        Franchise updated = existing.withName(newName);

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(existing));
        when(franchiseRepositoryPort.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        Mono<Franchise> result = useCase.execute(franchiseId, newName);

        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertThat(franchise.getId()).isEqualTo(franchiseId);
                    assertThat(franchise.getName()).isEqualTo(newName);
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(franchiseRepositoryPort, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException si la franquicia no existe")
    void shouldThrowNotFoundWhenFranchiseDoesNotExist() {
        Long franchiseId = 99L;
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<Franchise> result = useCase.execute(franchiseId, "Nuevo Nombre");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(franchiseRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el nuevo nombre es nulo o vacío")
    void shouldFailWhenNameIsInvalid() {
        Mono<Franchise> result = useCase.execute(1L, "   ");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(franchiseRepositoryPort, never()).findById(anyLong());
        verify(franchiseRepositoryPort, never()).save(any());
    }
}
