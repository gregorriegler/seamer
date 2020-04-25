package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileSignatureRepository;
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

    public <T> Seam<T> save(String seamId, Signature<T> signature) {
        Seams.<T>signatures(serializer).persist(seamId, signature);
        return createSeam(seamId, signature);
    }

    public <T> Optional<Seam<T>> byId(String seamId) {
        return Seams.<T>signatures(serializer)
            .byId(seamId)
            .map(signature -> createSeam(seamId, signature));
    }

    public <T> Optional<Seam<T>> proxySeamById(String seamId) {
        return Seams.<T>signatures(serializer)
            .proxyById(seamId)
            .map(signature -> createSeam(seamId, signature));
    }

    private static <T> FileSignatureRepository<T> signatures(KryoSerializer serializer) {
        return new FileSignatureRepository<T>(serializer);
    }

    private <T> Seam<T> createSeam(String seamId, Signature<T> signature) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(seamId, serializer)
        );
    }

}
