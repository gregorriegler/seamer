package com.gregorriegler.seamer.core;

import java.util.function.Function;

@FunctionalInterface
public interface SignatureWith1Argument<A1, R> extends Function<A1, R>, Signature<R> {

    @Override
    @SuppressWarnings("unchecked")
    default R invoke(Object... args) {
        return apply((A1) args[0]);
    }
}
