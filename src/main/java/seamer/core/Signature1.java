package seamer.core;

import java.util.function.Function;

@FunctionalInterface
public interface Signature1<A1, R> extends Function<A1, R>, Signature<R> {

    @Override
    @SuppressWarnings("unchecked")
    default R invoke(Object... args) {
        return apply((A1) args[0]);
    }
}
