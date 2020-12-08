package com.gregorriegler.seamer.core;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class SeamRecorder<T> {

    private final ArgCandidates argCandidates = new ArgCandidates();
    private final Seam<T> seam;

    public SeamRecorder(Seam<T> seam) {
        this.seam = seam;
    }

    public SeamRecorder<T> addArgCandidates(int i, Object... candidates) {
        argCandidates.addCandidates(i, asList(candidates));
        return this;
    }

    public SeamRecorder<T> addArgCandidates(int i, Supplier<List<Object>> supplier) {
        argCandidates.addCandidates(i, supplier);
        return this;
    }

    public void shuffleArgsAndRecord() {
        List<Object[]> argCombinations = argCandidates.shuffle();
        for (Object[] args : argCombinations) {
            seam.invokeAndRecord(args);
        }
    }
}
