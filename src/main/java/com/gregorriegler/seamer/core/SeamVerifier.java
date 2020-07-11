package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final String seamId;
    public Seam<T> seam;
    private final InvocationRepository invocations;


    public SeamVerifier(String seamId, Seam<T> seam, InvocationRepository invocations) {
        this.seamId = seamId;
        this.seam = seam;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll(seamId)) {
            T actual = seam.invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
