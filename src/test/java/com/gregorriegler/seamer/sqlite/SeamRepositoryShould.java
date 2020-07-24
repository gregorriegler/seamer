package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class SeamRepositoryShould {
    public static final String SEAM_ID = "seamId";
    private final SeamRepository repository = createRepository();
    private final Invocations invocationsStub = Stubs.invocations();
    private final Seam<String> expectedSeam = new Seam<>(SEAM_ID, Stubs.invokable(), invocationsStub);

    protected abstract SeamRepository createRepository();

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    void start_empty() {
        Optional<Seam<String>> seam = repository.byId(SEAM_ID, invocationsStub);

        assertThat(seam).isEmpty();
    }

    @Test
    void persist_and_retrieve_a_seam() {
        repository.persist(expectedSeam);

        Seam<String> seam = createRepository().<String>byId(SEAM_ID, invocationsStub).get();

        assertThat(seam.id()).isEqualTo(expectedSeam.id());
        assertThat(seam.invocations()).isEqualTo(expectedSeam.invocations());
        assertThat(seam.invokable().invoke()).isEqualTo(expectedSeam.invokable().invoke());
    }

    @Test
    void clear_the_repository() {
        repository.persist(expectedSeam);

        repository.clear();

        assertThat(repository.byId(SEAM_ID, invocationsStub)).isEmpty();
    }
}
