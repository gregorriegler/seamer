package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;

public class Seamer {

    public static <T> Seam<T> load(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(new KryoSerializer(KryoFactory.createKryo(carrierClass)))
            .load(seamId)
            .map(seam -> create(seam, carrierClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(new KryoSerializer(KryoFactory.createProxyKryo(carrierClass)))
            .loadProxy(seamId)
            .map(seam -> create(seam, carrierClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> intercept(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        Seam<T> seam = create(signature, carrierClass, seamId);
        new FileSeamRepository<T>(new KryoSerializer(KryoFactory.createKryo(carrierClass))).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> interceptProxy(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        Seam<T> seam = create(signature, carrierClass, seamId);
        new FileSeamRepository<T>(new KryoSerializer(KryoFactory.createProxyKryo(carrierClass))).persist(signature, seamId);
        return seam;
    }

    public static <T> Seam<T> create(Signature<T> signature, Class<?> carrierClass, final String seamId) {
        return new Seam<>(
            signature,
            new FileInvocationRepository(new KryoSerializer(KryoFactory.createKryo(carrierClass)), seamId)
        );
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
