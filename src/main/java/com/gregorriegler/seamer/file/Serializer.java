package com.gregorriegler.seamer.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Serializer {
    <T> void serialize(T object, OutputStream outputStream);

    <T> T deserialize(InputStream inputStream, Class<T> type);

    <T> List<T> deserializeList(InputStream inputStream, Class<T> type);

    default byte[] toBytesArray(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serialize(object, outputStream);
        return outputStream.toByteArray();
    }

    default <T> T fromBytesArray(byte[] bytes, Class<T> clazz) {
        return deserialize(new ByteArrayInputStream(bytes), clazz);
    }
}
