package seamer.core;

import java.util.function.Function;

@FunctionalInterface
public interface Seam1<A1, R> extends Function<A1, R>, Seam<R> {

    @Override
    @SuppressWarnings("unchecked")
    default R invoke(Object... args) {
        return apply((A1) args[0]);
    }
}
