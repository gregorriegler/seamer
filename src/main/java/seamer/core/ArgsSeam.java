package seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ArgsSeam<T> extends Function<Object[], T>, Seam<T>, Serializable {

    @Override
    default T invoke(Object... args) {
        return apply(args);
    }
}
