package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final SeamWithId<T> seamWithId;
    private final InvocationRepository invocations;

    public SeamVerifier(SeamWithId<T> seamWithId, InvocationRepository invocations) {
        this.seamWithId = seamWithId;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll(seamWithId.id())) {
            T actual = seamWithId.seam().invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
