package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.file.Serializer;
import com.gregorriegler.seamer.kryo.KryoFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteInvocationsShould {

    private final SqliteInvocations invocations = new SqliteInvocations();
    private final Invocation expectedInvocation = Invocation.of(new Object[]{"1", "2"}, "hello world!");
    private final String seamId = "seamId";

    @Test
    void start_empty() {
        List<Invocation> result = invocations.getAll(seamId);

        assertThat(result).isEmpty();
    }

    @Test
    void record_an_invocation() {
        invocations.record(seamId, expectedInvocation);

        List<Invocation> result = invocations.getAll(seamId);

        assertThat(result).containsExactly(expectedInvocation);
    }

    private static class SqliteInvocations implements Invocations {

        private final Sqlite sqlite;
        private final Serializer serializer;

        private SqliteInvocations() {
            sqlite = new Sqlite("jdbc:sqlite::memory:");
            sqlite.command("create table if not exists invocations (seam_id string not null primary key, invocation blob)");
            serializer = KryoFactory.createSerializer();
        }

        @Override
        public void record(String seamId, Invocation invocation) {
            sqlite.parameterizedCommand("insert into invocations (seam_id, invocation) values (?, ?)", seamId, serializer.toBytesArray(invocation));
        }

        @Override
        public List<Invocation> getAll(String seamId) {
            Optional<Invocation> invocation = sqlite.queryBytes("select invocation from invocations where seam_id = ?", seamId)
                .map(bytes -> serializer.fromBytesArray(bytes, Invocation.class));
            if (invocation.isPresent()) {
                return Arrays.asList(invocation.get());
            }
            return Collections.emptyList();
        }
    }
}
