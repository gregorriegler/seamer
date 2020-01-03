package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.Serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class KryoSerializer implements Serializer {
    private final Kryo kryo;

    public KryoSerializer(Class<?> carrierClass) {
        this.kryo = KryoFactory.createKryo(carrierClass);
    }

    public KryoSerializer() {
        this.kryo = KryoFactory.createKryo();
    }

    @Override
    public void serialize(Signature<?> signature, OutputStream outputStream) {
        Output fileOutput = new Output(outputStream);
        kryo.writeClassAndObject(fileOutput, signature);
        fileOutput.close();
    }

    @Override
    public void serialize(Invocation invocation, OutputStream outputStream) {
        Output fileOutput = new Output(outputStream);
        kryo.writeClassAndObject(fileOutput, invocation);
        fileOutput.close();
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        Input fileInput = new Input(inputStream);
        return kryo.readClassAndObject(fileInput);
    }

    @Override
    public List<Invocation> deserializeInvocations(InputStream inputStream) {
        Input input = new Input(inputStream);
        List<Invocation> invocations = new ArrayList<>();
        while (!input.eof()) {
            Invocation invocation = (Invocation) kryo.readClassAndObject(input);
            invocations.add(invocation);
        }
        return invocations;
    }
}