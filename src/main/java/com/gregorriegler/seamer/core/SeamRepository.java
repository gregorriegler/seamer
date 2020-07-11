package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(SeamWithId seamWithId);

    Optional<SeamWithId<T>> byId(String seamId);

    Optional<ProxySuture<T>> proxyById(String seamId);
}
