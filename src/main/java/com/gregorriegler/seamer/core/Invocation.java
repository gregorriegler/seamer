package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Invocation implements Serializable {

    private Object[] args;
    private Object result;

    public static Invocation of(Object[] args, Object result) {
        return new Invocation(args, result);
    }

    private Invocation() {
    }

    private Invocation(Object[] args, Object result) {
        this.args = args;
        this.result = result;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(args) + " => " + result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invocation that = (Invocation) o;
        return Arrays.equals(args, that.args) &&
            Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(result);
        result1 = 31 * result1 + Arrays.hashCode(args);
        return result1;
    }
}
