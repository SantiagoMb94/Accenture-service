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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBranchesByFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    private GetBranchesByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetBranchesByFranchiseUseCase(franchiseRepositoryPort, branchRepositoryPort);
    }

    @Test
    @DisplayName("Debe listar las sucursales de una franquicia existente")
    void shouldListBranchesOfFranchise() {
        Long franchiseId = 1L;
        Franchise franchise = new Franchise(franchiseId, "Franquicia A");
        Branch b1 = new Branch(10L, franchiseId, "Sucursal Norte");
        Branch b2 = new Branch(20L, franchiseId, "Sucursal Sur");

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(franchiseId)).thenReturn(Flux.just(b1, b2));

        Flux<Branch> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(branch -> assertThat(branch.getName()).isEqualTo("Sucursal Norte"))
                .assertNext(branch -> assertThat(branch.getName()).isEqualTo("Sucursal Sur"))
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, times(1)).findByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException si la franquicia no existe")
    void shouldThrowNotFoundWhenFranchiseDoesNotExist() {
        Long franchiseId = 99L;
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        Flux<Branch> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
        verify(branchRepositoryPort, never()).findByFranchiseId(anyLong());
    }

    @Test
    @DisplayName("Debe fallar si el ID de franquicia es nulo")
    void shouldFailWhenFranchiseIdIsNull() {
        Flux<Branch> result = useCase.execute(null);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(franchiseRepositoryPort, never()).findById(anyLong());
    }
}
