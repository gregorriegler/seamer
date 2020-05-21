package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamInterceptor<T> implements Serializable {

    private final Method<T> method;
    private final InvocationRepository invocations;

    public SeamInterceptor(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = execute(args);
        recordInvocation(args, result);
        return result;
    }

    public T execute(Object... args) {
        return method.invoke(args);
    }

    public void recordInvocation(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }
}
