package com.accenture.service.application.usecase.branch;

import com.accenture.service.domain.exception.BranchNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
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
class UpdateBranchNameUseCaseTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    private UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBranchNameUseCase(branchRepositoryPort);
    }

    @Test
    @DisplayName("Debe actualizar el nombre de una sucursal exitosamente")
    void shouldUpdateBranchNameSuccessfully() {
        Long branchId = 10L;
        String newName = "Sucursal Renovada";
        Branch existing = new Branch(branchId, 1L, "Sucursal Vieja");
        Branch updated = existing.withName(newName);

        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.just(existing));
        when(branchRepositoryPort.save(any(Branch.class))).thenReturn(Mono.just(updated));

        Mono<Branch> result = useCase.execute(branchId, newName);

        StepVerifier.create(result)
                .assertNext(branch -> {
                    assertThat(branch.getId()).isEqualTo(branchId);
                    assertThat(branch.getName()).isEqualTo(newName);
                })
                .verifyComplete();

        verify(branchRepositoryPort, times(1)).findById(branchId);
        verify(branchRepositoryPort, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe lanzar BranchNotFoundException si la sucursal no existe")
    void shouldThrowNotFoundWhenBranchDoesNotExist() {
        Long branchId = 99L;
        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.empty());

        Mono<Branch> result = useCase.execute(branchId, "Nuevo Nombre");

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepositoryPort, times(1)).findById(branchId);
        verify(branchRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el nuevo nombre es nulo o vacío")
    void shouldFailWhenNameIsInvalid() {
        Mono<Branch> result = useCase.execute(10L, "   ");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(branchRepositoryPort, never()).findById(anyLong());
        verify(branchRepositoryPort, never()).save(any());
    }
}
