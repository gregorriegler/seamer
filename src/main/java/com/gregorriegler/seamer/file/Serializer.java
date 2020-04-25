package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocation;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Serializer {
    <T> void serialize(T object, OutputStream outputStream);

    <T> T deserialize(InputStream inputStream, Class<T> type);

    void serializeInvocation(Invocation invocation, OutputStream outputStream);

    List<Invocation> deserializeInvocations(InputStream inputStream);
}
