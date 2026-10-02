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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetFranchiseByIdUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    private GetFranchiseByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetFranchiseByIdUseCase(franchiseRepositoryPort);
    }

    @Test
    @DisplayName("Debe retornar la franquicia cuando existe")
    void shouldReturnFranchiseWhenExists() {
        Long franchiseId = 1L;
        Franchise franchise = new Franchise(franchiseId, "Franquicia A");

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(f -> {
                    assertThat(f.getId()).isEqualTo(franchiseId);
                    assertThat(f.getName()).isEqualTo("Franquicia A");
                })
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
    }

    @Test
    @DisplayName("Debe lanzar FranchiseNotFoundException cuando no existe")
    void shouldThrowNotFoundWhenDoesNotExist() {
        Long franchiseId = 999L;
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<Franchise> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort, times(1)).findById(franchiseId);
    }
}
