package com.gregorriegler.seamer.core;

public class SeamExecutor<T> {

    private final Method<T> method;

    public SeamExecutor(Method<T> method) {
        this.method = method;
    }

    public T execute(Object... args) {
        return method.invoke(args);
    }
}
