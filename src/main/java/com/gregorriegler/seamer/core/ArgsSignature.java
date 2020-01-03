package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ArgsSignature<T> extends Function<Object[], T>, Signature<T>, Serializable {

    @Override
    default T invoke(Object... args) {
        return apply(args);
    }
}
