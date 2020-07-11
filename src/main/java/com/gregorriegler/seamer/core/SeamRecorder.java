package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private final String seamId;
    private final Seam<T> seam;
    private final InvocationRepository invocations;

    public SeamRecorder(String seamId, Seam<T> seam, InvocationRepository invocations) {
        this.seamId = seamId;
        this.seam = seam;
        this.invocations = invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    public T invoke(Object... args) {
        return seam.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(seamId, Invocation.of(args, result));
    }
}
