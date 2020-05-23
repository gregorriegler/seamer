package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final InvocationRepository invocations;

    public Seam<T> seam;

    public SeamVerifier(Seam<T> seam, InvocationRepository invocations) {
        this.seam = seam;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll()) {
            T actual = seam.invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
