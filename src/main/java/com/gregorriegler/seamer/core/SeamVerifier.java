package com.gregorriegler.seamer.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeamVerifier<T> {
    private final Seam<T> seam;
    private final Invocations invocations;

    public SeamVerifier(Seam<T> seam, Invocations invocations) {
        this.seam = seam;
        this.invocations = invocations;
    }

    public void verify() {
        for (Invocation invocation : invocations.getAll(seam.id())) {
            T actual = seam.seam().invoke(invocation.getArgs());
            assertThat(actual, equalTo(invocation.getResult()));
        }
    }

}
