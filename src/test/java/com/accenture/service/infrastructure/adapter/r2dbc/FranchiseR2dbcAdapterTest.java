package com.accenture.service.infrastructure.adapter.r2dbc;

import com.accenture.service.domain.model.Franchise;
import com.accenture.service.infrastructure.adapter.r2dbc.entity.FranchiseEntity;
import com.accenture.service.infrastructure.adapter.r2dbc.repository.FranchiseR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseR2dbcAdapterTest {

    @Mock
    private FranchiseR2dbcRepository repository;

    private FranchiseR2dbcAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseR2dbcAdapter(repository);
    }

    @Test
    @DisplayName("Debe guardar y mapear una franquicia correctamente")
    void shouldSaveFranchise() {
        Franchise domain = new Franchise("Franquicia A");
        FranchiseEntity entity = new FranchiseEntity(1L, "Franquicia A", Instant.now());

        when(repository.save(any(FranchiseEntity.class))).thenReturn(Mono.just(entity));

        Mono<Franchise> result = adapter.save(domain);

        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertThat(saved.getId()).isEqualTo(1L);
                    assertThat(saved.getName()).isEqualTo("Franquicia A");
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(FranchiseEntity.class));
    }

    @Test
    @DisplayName("Debe buscar una franquicia por ID")
    void shouldFindById() {
        FranchiseEntity entity = new FranchiseEntity(1L, "Franquicia A", Instant.now());
        when(repository.findById(1L)).thenReturn(Mono.just(entity));

        Mono<Franchise> result = adapter.findById(1L);

        StepVerifier.create(result)
                .assertNext(found -> assertThat(found.getName()).isEqualTo("Franquicia A"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe buscar una franquicia por nombre")
    void shouldFindByName() {
        FranchiseEntity entity = new FranchiseEntity(1L, "Franquicia A", Instant.now());
        when(repository.findByName("Franquicia A")).thenReturn(Mono.just(entity));

        Mono<Franchise> result = adapter.findByName("Franquicia A");

        StepVerifier.create(result)
                .assertNext(found -> assertThat(found.getId()).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe listar todas las franquicias")
    void shouldFindAll() {
        FranchiseEntity entity = new FranchiseEntity(1L, "Franquicia A", Instant.now());
        when(repository.findAll()).thenReturn(Flux.just(entity));

        Flux<Franchise> result = adapter.findAll();

        StepVerifier.create(result)
                .assertNext(found -> assertThat(found.getName()).isEqualTo("Franquicia A"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe verificar existencia por ID")
    void shouldCheckExistsById() {
        when(repository.existsById(1L)).thenReturn(Mono.just(true));

        Mono<Boolean> result = adapter.existsById(1L);

        StepVerifier.create(result)
                .assertNext(exists -> assertThat(exists).isTrue())
                .verifyComplete();
    }
}
