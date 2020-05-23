package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final InvocationRepository invocations;

    public Method<T> method;

    public SeamVerifier(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll()) {
            T actual = method.invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
