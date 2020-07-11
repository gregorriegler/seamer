package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(Seam seam);

    Optional<Seam<T>> byId(String seamId);

    Optional<ProxySuture<T>> proxyById(String seamId);
}
