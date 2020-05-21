package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private final SeamExecutor<T> executor;
    private final InvocationRepository invocations;

    public SeamRecorder(SeamExecutor<T> executor, InvocationRepository invocations) {
        this.invocations = invocations;
        this.executor = executor;
    }

    public T invokeAndRecord(Object... args) {
        T result = execute(args);
        recordInvocation(args, result);
        return result;
    }

    public T execute(Object... args) {
        return executor.execute(args);
    }

    public void recordInvocation(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }
}
