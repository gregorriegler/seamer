package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private final SeamInvoker<T> invoker;
    private final InvocationRepository invocations;

    public SeamRecorder(SeamInvoker<T> invoker, InvocationRepository invocations) {
        this.invocations = invocations;
        this.invoker = invoker;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    public T invoke(Object... args) {
        return invoker.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }
}
