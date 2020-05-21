package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ArgsMethod<T> extends Function<Object[], T>, Method<T>, Serializable {

    @Override
    default T invoke(Object... args) {
        return apply(args);
    }
}
