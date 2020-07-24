package com.gregorriegler.seamer.core;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.kryo.KryoFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class SeamRepositoryShould {
    private final SeamRepository repository = createSeams();
    private final Invocations invocationsStub = Stubs.invocations();
    private final Seam<String> expectedSeam = Stubs.seam(invocationsStub);

    protected abstract Persistence createPersistence();

    @AfterEach
    void tearDown() {
        repository.remove(Stubs.SEAM_ID);
    }

    @Test
    void start_empty() {
        Optional<Seam<String>> seam = repository.byId(Stubs.SEAM_ID, invocationsStub);

        assertThat(seam).isEmpty();
    }

    @Test
    void persist_and_retrieve_a_seam() {
        repository.persist(expectedSeam);

        Seam<String> seam = createSeams().<String>byId(Stubs.SEAM_ID, invocationsStub).get();

        assertThat(seam.id()).isEqualTo(expectedSeam.id());
        assertThat(seam.invocations()).isEqualTo(expectedSeam.invocations());
        assertThat(seam.invokable().invoke()).isEqualTo(expectedSeam.invokable().invoke());
    }

    @Test
    void delete_a_seam() {
        repository.persist(expectedSeam);

        repository.remove(Stubs.SEAM_ID);

        assertThat(repository.byId(Stubs.SEAM_ID, invocationsStub)).isEmpty();
    }

    private SeamRepository createSeams() {
        return createPersistence().createSeams(KryoFactory.createSerializer());
    }
}