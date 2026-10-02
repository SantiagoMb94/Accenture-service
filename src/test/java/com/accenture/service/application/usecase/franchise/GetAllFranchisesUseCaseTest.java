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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllFranchisesUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    private GetAllFranchisesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllFranchisesUseCase(franchiseRepositoryPort);
    }

    @Test
    @DisplayName("Debe listar todas las franquicias registradas")
    void shouldListAllFranchises() {
        Franchise f1 = new Franchise(1L, "Franquicia 1");
        Franchise f2 = new Franchise(2L, "Franquicia 2");

        when(franchiseRepositoryPort.findAll()).thenReturn(Flux.just(f1, f2));

        Flux<Franchise> result = useCase.execute();

        StepVerifier.create(result)
                .assertNext(f -> assertThat(f.getName()).isEqualTo("Franquicia 1"))
                .assertNext(f -> assertThat(f.getName()).isEqualTo("Franquicia 2"))
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findAll();
    }
}
