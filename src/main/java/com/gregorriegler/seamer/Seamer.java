package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seamer {

    public static <T> Seam<T> load(Class<?> capturingClass, final String seamId) {
        return Seamer.<T>seams(capturingClass)
            .byId(seamId)
            .map(seam -> create(seam, capturingClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(Class<?> capturingClass, final String seamId) {
        return Seamer.<T>seams(capturingClass)
            .proxyById(seamId)
            .map(seam -> create(seam, capturingClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> intercept(Signature<T> signature, Class<?> capturingClass, final String seamId) {
        Seam<T> seam = create(signature, capturingClass, seamId);
        Seamer.<T>seams(capturingClass).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> create(Signature<T> signature, Class<?> capturingClass, final String seamId) {
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
