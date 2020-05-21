package com.gregorriegler.seamer.core;

import java.util.Optional;

public interface MethodRepository<T> {

    void persist(String seamId, Method<T> method);

    Optional<Method<T>> byId(String seamId);

    Optional<ProxyMethod<T>> proxyById(String seamId);
}
