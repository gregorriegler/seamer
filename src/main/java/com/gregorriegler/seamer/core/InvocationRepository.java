package com.gregorriegler.seamer.core;

import java.util.List;

public interface InvocationRepository {
    void record(String seamId, Invocation invocation);

    List<Invocation> getAll(String seamId);
}
