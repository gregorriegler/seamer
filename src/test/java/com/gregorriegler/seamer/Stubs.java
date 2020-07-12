package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;

import java.util.Collections;
import java.util.List;

public class Stubs {
    public static Invocations invocations() {
        return new InvocationsStub();
    }

    public static Invokable<String> invokable() {
        return (Invokable<String>) args -> "hello world!";
    }

    public static class InvocationsStub implements Invocations {

        @Override
        public void record(String seamId, Invocation invocation) {

        }

        @Override
        public List<Invocation> getAll(String seamId) {
            return Collections.emptyList();
        }

        @Override
        public int hashCode() {
            return 1337;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof InvocationsStub;
        }
    }
}
