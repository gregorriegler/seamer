package com.gregorriegler.seamer.core;

public class SeamTbd<T> {

    private final InvocationRepository invocations;
    private final SeamExecutor<T> executor;
    public Method<T> method;

    public SeamTbd(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
        this.executor = new SeamExecutor<>(method);
    }

    public SeamTbd<T> recordInvocation(Object... args) {
        T result = execute(args);
        record(args, result);
        return this;
    }

    public T execute(Object... args) {
        return executor.execute(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }
}
