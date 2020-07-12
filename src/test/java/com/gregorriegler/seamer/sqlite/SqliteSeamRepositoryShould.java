package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.Serializer;
import com.gregorriegler.seamer.kryo.KryoFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    private static class SqliteSeamRepository<T> implements SeamRepository<T> {

        private final Sqlite sqlite;
        private final Serializer serializer;

        public SqliteSeamRepository() {
            serializer = KryoFactory.createSerializer();
            sqlite = new Sqlite("jdbc:sqlite::memory:");
            sqlite.command(
                "drop table if exists seams",
                "create table seams (id string, invokable blob)"
            );
        }

        @Override
        public void persist(Seam<T> seam) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            serializer.serialize(seam.invokable(), outputStream);

            sqlite.parameterizedCommand("insert into seams (id, invokable) values (?, ?)", seam.id(), outputStream.toByteArray());
        }

        @Override
        public Optional<Seam<T>> byId(String seamId, Invocations invocations) {
            return sqlite.queryBytes("select invokable from seams where id = ?", seamId)
                .map(b -> new Seam<T>(seamId, invokableFrom(b), invocations));
        }

        private Invokable<T> invokableFrom(byte[] bytes) {
            return (Invokable<T>) serializer.deserialize(new ByteArrayInputStream(bytes), Invokable.class);
        }
    }
}
