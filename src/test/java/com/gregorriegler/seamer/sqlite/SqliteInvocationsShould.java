package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.kryo.KryoFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteInvocationsShould {

    private final Invocations invocations = createInvocations();

    protected Invocations createInvocations() {
        return createPersistence().createInvocations(KryoFactory.createSerializer());
    }

    protected SqlitePersistence createPersistence() {
        return new SqlitePersistence();
    }

    private final Invocation expectedInvocation = Invocation.of(new Object[]{"1", "2"}, "hello world!");
    private final Invocation anotherInvocation = Invocation.of(new Object[]{"1", "2", "3"}, "another world!");
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

    @Test
    void record_multiple_invocations() {
        invocations.record(seamId, expectedInvocation);
        invocations.record(seamId, anotherInvocation);

        List<Invocation> result = invocations.getAll(seamId);

        assertThat(result).containsExactly(expectedInvocation, anotherInvocation);
    }

}
