package com.gregorriegler.seamer.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Serializer {
    <T> void serialize(T object, OutputStream outputStream);

    <T> T deserialize(InputStream inputStream, Class<T> type);

    <T> List<T> deserializeList(InputStream inputStream, Class<T> type);
}
