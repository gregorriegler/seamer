package seamer.core;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface Seam<T> extends Function<Object[], T>, Serializable {
}
