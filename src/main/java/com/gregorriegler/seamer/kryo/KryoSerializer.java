package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.file.Serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class KryoSerializer implements Serializer {
    private final Kryo kryo;

    public KryoSerializer(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    public <T> void serialize(T object, OutputStream outputStream) {
        Output output = new Output(outputStream);
        kryo.writeObject(output, object);
        output.close();
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> type) {
        return kryo.readObject(new Input(inputStream), type);
    }

    @Override
    public void serializeInvocation(Invocation invocation, OutputStream outputStream) {
        serialize(invocation, outputStream);
    }

    @Override
    public List<Invocation> deserializeInvocations(InputStream inputStream) {
        Input input = new Input(inputStream);
        List<Invocation> invocations = new ArrayList<>();
        while (!input.end()) {
            Invocation invocation = kryo.readObject(input, Invocation.class);
            invocations.add(invocation);
        }
        return invocations;
    }
}