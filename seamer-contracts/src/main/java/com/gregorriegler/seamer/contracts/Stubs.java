package com.gregorriegler.seamer.contracts;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;

import java.util.Collections;
import java.util.List;

public class Stubs {
    public static final String SEAM_ID = "seamId";

    public static Invocations invocations() {
        return new InvocationsStub();
    }

    public static Invokable<String> invokable() {
        return args -> "hello world!";
    }

    public static Seam<String> seam(Invocations invocationsStub) {
        return new Seam<>(SEAM_ID, invokable(), invocationsStub);
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
        public void remove(String seamId) {
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
