package com.gregorriegler.seamer.core;

@FunctionalInterface
public interface MethodWith3Arguments<A1, A2, A3, R> extends Method<R> {

    R apply(A1 a1, A2 a2, A3 a3);

    @Override
    @SuppressWarnings("unchecked")
    default R invoke(Object... args) {
        return apply((A1) args[0], (A2) args[1], (A3) args[2]);
    }
}
