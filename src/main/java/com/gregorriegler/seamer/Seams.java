package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamExecutor;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileMethodRepository;
import com.gregorriegler.seamer.kryo.KryoSerializer;

import java.util.Optional;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seams {

    private final KryoSerializer serializer;

    public static Seams of(Class<?> capturingClass) {
        return new Seams(capturingClass);
    }

    private Seams(Class<?> capturingClass) {
        serializer = createSerializer(capturingClass);
    }

    public <T> void add(String seamId, Method<T> method) {
        Seams.<T>methods(serializer).persist(seamId, method);
    }

    public <T> Optional<Seam<T>> byId(String seamId) {
        return Seams.<T>methods(serializer)
            .byId(seamId)
            .map(signature -> createSeam(seamId, signature));
    }

    public <T> Optional<Seam<T>> proxySeamById(String seamId) {
        return Seams.<T>methods(serializer)
            .proxyById(seamId)
            .map(signature -> createSeam(seamId, signature));
    }

    private static <T> FileMethodRepository<T> methods(KryoSerializer serializer) {
        return new FileMethodRepository<T>(serializer);
    }

    public <T> Seam<T> createSeam(String seamId, Method<T> method) {
        return new Seam<>(
            method,
            new FileInvocationRepository(seamId, serializer)
        );
    }

    public <T> SeamRecorder<T> createInterceptor(String seamId, Method<T> method) {
        return new SeamRecorder<>(
            new SeamExecutor<>(method), new FileInvocationRepository(seamId, serializer)
        );
    }

}
