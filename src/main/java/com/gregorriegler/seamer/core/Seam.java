package com.gregorriegler.seamer.core;

import org.junit.jupiter.params.provider.Arguments;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Seam<T> implements Serializable {

    private final InvocationRepository invocations;
    private final ArgCandidates argCandidates = new ArgCandidates();
    private final SeamRecorder<T> recorder;
    private final SeamExecutor<T> executor;
    private final SeamVerifier<T> verifier;

    public Method<T> method;

    public Seam(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
        this.executor = new SeamExecutor<>(method);
        this.recorder = new SeamRecorder<>(executor, invocations);
        this.verifier = new SeamVerifier<>(method, invocations);
    }

    public Stream<Arguments> invocationsAsJupiterArguments() {
        return invocations.getAll()
            .stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    public void verify() {
        this.verifier.verify();
    }

    public T execute(Object... args) {
        return executor.execute(args);
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
