package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;

public class Seamer {

    public static <T> Seam<T> load(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(KryoFactory.createSerializer(carrierClass))
            .load(seamId)
            .map(seam -> create(seam, carrierClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(KryoFactory.createSerializer(carrierClass))
            .loadProxy(seamId)
            .map(seam -> create(seam, carrierClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> intercept(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        Seam<T> seam = create(signature, carrierClass, seamId);
        new FileSeamRepository<T>(KryoFactory.createSerializer(carrierClass)).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> create(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(KryoFactory.createSerializer(carrierClass), seamId)
        );
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
