package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final InvocationRepository invocations;
    private final SeamInvoker<T> invoker;

    public Method<T> method;

    public SeamVerifier(Method<T> method, InvocationRepository invocations) {
        this.method = method;
        this.invocations = invocations;
        this.invoker = new SeamInvoker<>(method);
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll()) {
            T actual = invoker.invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
