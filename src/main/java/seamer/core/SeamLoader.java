package seamer.core;

import java.util.Optional;

public interface SeamLoader<T> {

    Optional<Seam<T>> load(String seamId, Class<?> aClass);
}
