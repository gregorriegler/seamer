package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ArgsInvokable<T> extends Function<Object[], T>, Invokable<T>, Serializable {

    @Override
    default T invoke(Object... args) {
        return apply(args);
    }
}
