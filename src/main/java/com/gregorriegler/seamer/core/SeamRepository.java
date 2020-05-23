package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(String seamId, Seam<T> seam);

    Optional<Seam<T>> byId(String seamId);

    Optional<ProxySeam<T>> proxyById(String seamId);
}
