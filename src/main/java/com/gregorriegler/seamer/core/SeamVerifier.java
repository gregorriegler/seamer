package com.gregorriegler.seamer.core;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final InvocationRepository invocations;
    private final SeamExecutor<T> executor;

    public Method<T> method;

    public SeamVerifier(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
        this.executor = new SeamExecutor<>(method);
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll()) {
            T actual = execute(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

    public T execute(Object... args) {
        return executor.execute(args);
    }

    public List<Invocation> getInvocations() {
        return invocations.getAll();
    }

}
