package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(String seamId, Signature<T> signature);

    Optional<Signature<T>> byId(String seamId);

    Optional<ProxySignature<T>> proxyById(String seamId);
}
