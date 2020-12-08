package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gregorriegler.seamer.core.Serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KryoSerializer implements Serializer {
    private final Kryo kryo;

    public KryoSerializer(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    public <T> void serialize(T object, OutputStream outputStream) {
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> type) {
        return (T) kryo.readClassAndObject(new Input(inputStream));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> deserializeList(InputStream inputStream, Class<T> type) {
        Input input = new Input(inputStream);
        List<T> result = new ArrayList<>();
        while (!input.end()) {
            T object = (T) kryo.readClassAndObject(input);
            result.add(object);
        }
        return Collections.unmodifiableList(result);
    }
}