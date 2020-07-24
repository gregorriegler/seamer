package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository {

    <T> void persist(Seam<T> seam);

    <T> Optional<Seam<T>> byId(String seamId, Invocations invocations);

    void remove(String seamId);
}
