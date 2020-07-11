package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(Seam<T> seam);

    Optional<Seam<T>> byId(String seamId, Invocations invocations);

}
