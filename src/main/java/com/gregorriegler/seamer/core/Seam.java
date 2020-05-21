package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class Seam<T> implements Serializable {

    private final InvocationRepository invocations;
    private final ArgCandidates argCandidates = new ArgCandidates();

    public Method<T> method;

    public Seam(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll()) {
            T actual = execute(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

    public Seam<T> recordInvocation(Object... args) {
        T result = execute(args);
        record(args, result);
        return this;
    }

    public T execute(Object... args) {
        return method.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(Invocation.of(args, result));
    }

    public List<Invocation> getInvocations() {
        return invocations.getAll();
    }

    public Seam<T> addArgCandidates(int i, Object... candidates) {
        argCandidates.addCandidates(i, asList(candidates));
        return this;
    }

    public Seam<T> addArgCandidates(int i, Supplier<List<Object>> supplier) {
        argCandidates.addCandidates(i, supplier);
        return this;
    }

    public void shuffleArgsAndExecute() {
        List<Object[]> argCombinations = argCandidates.shuffle();
        for (Object[] args : argCombinations) {
            recordInvocation(args);
        }
    }

}
