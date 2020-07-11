package com.gregorriegler.seamer.core;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class SeamRecordingsBuilder<T> {

    private final ArgCandidates argCandidates = new ArgCandidates();
    private final SeamRecorder<T> recorder;

    public SeamRecordingsBuilder(String seamId, Seam<T> seam, InvocationRepository invocations) {
        this.recorder = new SeamRecorder<>(seamId, seam, invocations);
    }

    public SeamRecordingsBuilder<T> addArgCandidates(int i, Object... candidates) {
        argCandidates.addCandidates(i, asList(candidates));
        return this;
    }

    public SeamRecordingsBuilder<T> addArgCandidates(int i, Supplier<List<Object>> supplier) {
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
