package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoSerializer;

public class Seamer {

    public static <T> Seam<T> load(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(new KryoSerializer(carrierClass))
            .load(seamId)
            .map(seam -> create(seam, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> persist(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        reset(seamId);
        return intercept(signature, carrierClass, seamId);
    }

    public static <T> Seam<T> intercept(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        Seam<T> seam = create(signature, seamId);
        new FileSeamRepository<T>(new KryoSerializer(carrierClass)).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> create(Signature<T> signature, final String seamId) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(new KryoSerializer(), seamId)
        );
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
