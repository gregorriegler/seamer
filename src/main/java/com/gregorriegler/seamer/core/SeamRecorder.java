package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private SeamWithId<T> seamWithId;
    private final InvocationRepository invocations;

    public SeamRecorder(SeamWithId<T> seamWithId, InvocationRepository invocations) {
        this.seamWithId = seamWithId;
        this.invocations = invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    // todo encapsulation
    public T invoke(Object... args) {
        return seamWithId.seam().invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(seamWithId.id(), Invocation.of(args, result));
    }
}
