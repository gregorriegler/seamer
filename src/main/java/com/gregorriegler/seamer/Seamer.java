package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer {

    public static <T> SeamRecorder<T> intercept(final String seamId, Class<?> capturingClass, Method<T> method) {
        Seams seams = Seams.of(capturingClass);
        seams.add(seamId, method);
        return seams.createInterceptor(seamId, method);
    }

    public static <T> Seam<T> load(final String seamId, Class<?> capturingClass) {
        return Seams.of(capturingClass).<T>byId(seamId).orElseThrow(FailedToLoad::new);
    }

    public static <T> Seam<T> loadProxy(final String seamId, Class<?> capturingClass) {
        return Seams.of(capturingClass).<T>proxySeamById(seamId).orElseThrow(FailedToLoad::new);
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static void verify(String seamId, Class<?> capturingClass) {
        load(seamId, capturingClass).verify();
    }

    public static void verifyProxy(String seamId, Class<?> capturingClass) {
        loadProxy(seamId, capturingClass).verify();
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
