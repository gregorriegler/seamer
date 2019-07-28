package seamer.core;

import java.io.Serializable;
import java.util.Arrays;

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
}
