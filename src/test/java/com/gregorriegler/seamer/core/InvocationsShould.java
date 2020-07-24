package com.gregorriegler.seamer.core;

import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.sqlite.SqlitePersistence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class InvocationsShould {
    private final Invocations invocations = createInvocations();
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

    private Invocations createInvocations() {
        return createPersistence().createInvocations(KryoFactory.createSerializer());
    }

    protected abstract SqlitePersistence createPersistence();
}
