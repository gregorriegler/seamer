package seamer;

import java.io.Serializable;
import java.util.Arrays;

public class Call implements Serializable {

    private Object[] args;
    private Object result;

    public static Call of(Object[] args, Object result) {
        return new Call(args, result);
    }

    private Call() {
    }

    private Call(Object[] args, Object result) {
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
