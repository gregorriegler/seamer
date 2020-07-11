package com.gregorriegler.seamer.core;

import java.io.Serializable;

public class SeamRecorder<T> implements Serializable {

    private Seam<T> seam;
    private final InvocationRepository invocations;

    public SeamRecorder(Seam<T> seam, InvocationRepository invocations) {
        this.seam = seam;
        this.invocations = invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    // todo encapsulation
    public T invoke(Object... args) {
        return seam.seam().invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(seam.id(), Invocation.of(args, result));
    }
}
