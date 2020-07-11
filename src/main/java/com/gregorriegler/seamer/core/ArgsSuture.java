package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ArgsSuture<T> extends Function<Object[], T>, Suture<T>, Serializable {

    @Override
    default T invoke(Object... args) {
        return apply(args);
    }
}
