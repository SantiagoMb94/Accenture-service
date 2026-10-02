package com.accenture.service.application.usecase.branch;

import com.accenture.service.domain.exception.FranchiseNotFoundException;
import com.accenture.service.domain.model.Branch;
import com.accenture.service.domain.model.Franchise;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
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
class AddBranchUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    private AddBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddBranchUseCase(franchiseRepositoryPort, branchRepositoryPort);
    }

    @Test
    @DisplayName("Debe agregar una sucursal a una franquicia existente exitosamente")
    void shouldAddBranchSuccessfully() {
        Long franchiseId = 1L;
        String branchName = "Sucursal Norte";
        Franchise franchise = new Franchise(franchiseId, "Franquicia A");
        Branch savedBranch = new Branch(10L, franchiseId, branchName);

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.save(any(Branch.class))).thenReturn(Mono.just(savedBranch));

        Mono<Branch> result = useCase.execute(franchiseId, branchName);

        StepVerifier.create(result)
                .assertNext(branch -> {
                    assertThat(branch.getId()).isEqualTo(10L);
                    assertThat(branch.getFranchiseId()).isEqualTo(franchiseId);
                    assertThat(branch.getName()).isEqualTo(branchName);
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException si la franquicia no existe")
    void shouldThrowNotFoundWhenFranchiseDoesNotExist() {
        Long franchiseId = 99L;
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<Branch> result = useCase.execute(franchiseId, "Sucursal");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el nombre de la sucursal está vacío")
    void shouldFailWhenBranchNameIsEmpty() {
        Mono<Branch> result = useCase.execute(1L, "   ");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(franchiseRepositoryPort, never()).findById(anyLong());
        verify(branchRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el ID de franquicia es nulo")
    void shouldFailWhenFranchiseIdIsNull() {
        Mono<Branch> result = useCase.execute(null, "Sucursal");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(franchiseRepositoryPort, never()).findById(anyLong());
        verify(branchRepositoryPort, never()).save(any());
    }
}
