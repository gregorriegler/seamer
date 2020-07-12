package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Seam;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteSeamRepositoryShould {

    private final SqliteSeamRepository<String> repository = new SqliteSeamRepository<>();
    private final Invocations invocationsStub = Stubs.invocations();

    @Test
    void start_empty() {
        Optional<Seam<String>> seam = repository.byId("seamId", invocationsStub);

        assertThat(seam).isEmpty();
    }

    @Test
    void persist_and_retrieve_a_seam() {
        Seam<String> expectedSeam = new Seam<>("seamId", Stubs.invokable(), invocationsStub);

        repository.persist(expectedSeam);
        Seam<String> seam = repository.byId("seamId", invocationsStub).get();

        assertThat(seam.id()).isEqualTo(expectedSeam.id());
        assertThat(seam.invocations()).isEqualTo(expectedSeam.invocations());
        assertThat(seam.invokable().invoke()).isEqualTo(expectedSeam.invokable().invoke());
    }

}
