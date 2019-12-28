package seamer.core;

import java.util.List;

public interface InvocationRepository {
    void record(Invocation invocation);

    List<Invocation> getAll();
}
