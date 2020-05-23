package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class Seam<T> implements Serializable {

    private final ArgCandidates argCandidates = new ArgCandidates();
    private final SeamRecorder<T> recorder;

    public Seam(Method<T> method, InvocationRepository invocations) {
        this.recorder = new SeamRecorder<>(method, invocations);
    }

    public Seam<T> addArgCandidates(int i, Object... candidates) {
        argCandidates.addCandidates(i, asList(candidates));
        return this;
    }

    public Seam<T> addArgCandidates(int i, Supplier<List<Object>> supplier) {
        argCandidates.addCandidates(i, supplier);
        return this;
    }

    public void shuffleArgsAndRecord() {
        List<Object[]> argCombinations = argCandidates.shuffle();
        for (Object[] args : argCombinations) {
            recorder.invokeAndRecord(args);
        }
    }
}
