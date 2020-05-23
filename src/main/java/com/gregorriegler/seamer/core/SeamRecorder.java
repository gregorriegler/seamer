package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private final Method<T> method;
    private final InvocationRepository invocations;

    public SeamRecorder(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    public T invoke(Object... args) {
        return method.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }
}
