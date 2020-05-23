package com.gregorriegler.seamer.core;

public class SeamInvoker<T> {

    private final Method<T> method;

    public SeamInvoker(Method<T> method) {
        this.method = method;
    }

    public T invoke(Object... args) {
        return method.invoke(args);
    }
}
