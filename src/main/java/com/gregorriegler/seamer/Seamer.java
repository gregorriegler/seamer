package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.Signature;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer {

    public static <T> Seam<T> intercept(final String seamId, Class<?> capturingClass, Signature<T> signature) {
        return new Seams(capturingClass).save(seamId, signature);
    }

    public static <T> Seam<T> load(final String seamId, Class<?> capturingClass) {
        return new Seams(capturingClass).<T>byId(seamId).orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(final String seamId, Class<?> capturingClass) {
        return new Seams(capturingClass).<T>proxySeamById(seamId).orElseThrow(FailedToLoad::new);
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
