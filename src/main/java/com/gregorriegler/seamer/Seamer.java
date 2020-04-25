package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seamer {

    public static <T> Seam<T> load(final String seamId, Class<?> capturingClass) {
        return Seamer.<T>seams(capturingClass)
            .byId(seamId)
            .map(seam -> create(seamId, seam, capturingClass))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(final String seamId, Class<?> capturingClass) {
        return Seamer.<T>seams(capturingClass)
            .proxyById(seamId)
            .map(seam -> create(seamId, seam, capturingClass))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> intercept(final String seamId, Signature<T> signature, Class<?> capturingClass) {
        Seamer.<T>seams(capturingClass).persist(seamId, signature);
        return create(seamId, signature, capturingClass);
    }

    public static <T> Seam<T> create(final String seamId, Signature<T> signature, Class<?> capturingClass) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(createSerializer(capturingClass), seamId)
        );
    }

    public static <T> FileSeamRepository<T> seams(Class<?> capturingClass) {
        return new FileSeamRepository<>(createSerializer(capturingClass));
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
