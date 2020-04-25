package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileSignatureRepository;

import java.util.Optional;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seams {

    private final Class<?> capturingClass;

    public Seams(Class<?> capturingClass) {
        this.capturingClass = capturingClass;
    }

    public <T> Seam<T> save(String seamId, Signature<T> signature) {
        Seams.<T>signatures(this.capturingClass).persist(seamId, signature);
        return create(seamId, this.capturingClass, signature);
    }

    public <T> Optional<Seam<T>> byId(String seamId) {
        return Seams.<T>signatures(this.capturingClass)
            .byId(seamId)
            .map(seam -> create(seamId, this.capturingClass, seam));
    }

    public <T> Optional<Seam<T>> proxySeamById(String seamId) {
        return Seams.<T>signatures(this.capturingClass)
            .proxyById(seamId)
            .map(seam -> create(seamId, this.capturingClass, seam));
    }

    private static <T> Seam<T> create(final String seamId, Class<?> capturingClass, Signature<T> signature) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(createSerializer(capturingClass), seamId)
        );
    }

    private static <T> FileSignatureRepository<T> signatures(Class<?> capturingClass) {
        return new FileSignatureRepository<>(createSerializer(capturingClass));
    }
}
