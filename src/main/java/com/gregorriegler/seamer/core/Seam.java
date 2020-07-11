package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class Seam<T> {

    private final String id;
    private final Invokable<T> invokable;
    private final Invocations invocations;

    public Seam(String id, Invokable<T> invokable, Invocations invocations) {
        assertNotNull(id, "id is mandatory");
        assertNotNull(invokable, "seam is mandatory");
        assertNotNull(invocations, "invocations are mandatory");
        this.id = id;
        this.invokable = invokable;
        this.invocations = invocations;
    }

    public String id() {
        return id;
    }

    public Invokable<T> seam() {
        return invokable;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    public T invoke(Object... args) {
        return invokable.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(id, Invocation.of(args, result));
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll(id)) {
            T actual = invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

    private void assertNotNull(Object var, String message) {
        if(var == null) throw new IllegalArgumentException(message);
    }
}
