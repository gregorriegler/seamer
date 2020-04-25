package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Signature;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Serializer {
    void serialize(Signature<?> signature, OutputStream outputStream);

    void serializeInvocation(Invocation invocation, OutputStream outputStream);

    Object deserialize(InputStream inputStream);

    List<Invocation> deserializeInvocations(InputStream inputStream);

    <T> void serializeObject(T object, OutputStream outputStream);

    <T> T deserializeObject(InputStream inputStream, Class<T> type);
}
