package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;

public class Seamer {

    public static <T> Seam<T> load(Class<?> capturingClass, final String seamId) {
        return new FileSeamRepository<T>(KryoFactory.createSerializer(capturingClass))
            .load(seamId)
            .map(seam -> create(seam, capturingClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(Class<?> capturingClass, final String seamId) {
        return new FileSeamRepository<T>(KryoFactory.createSerializer(capturingClass))
            .loadProxy(seamId)
            .map(seam -> create(seam, capturingClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> intercept(Signature<T> signature, Class<?> capturingClass, final String seamId) {
        Seam<T> seam = create(signature, capturingClass, seamId);
        new FileSeamRepository<T>(KryoFactory.createSerializer(capturingClass)).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> create(Signature<T> signature, Class<?> capturingClass, final String seamId) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(KryoFactory.createSerializer(capturingClass), seamId)
        );
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
