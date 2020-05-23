package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.file.FileResetter;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class Seamer {

    public static <T> SeamRecorder<T> intercept(final String seamId, Method<T> method) {
        Seams seams = Seams.of();
        seams.add(seamId, method);
        return seams.createInterceptor(seamId, method);
    }

    public static Stream<Arguments> invocationsAsArguments(String seamId) {
        return load(seamId).invocationsAsArguments();
    }

    public static void verify(String seamId) {
        load(seamId).verify();
    }

    public static <T> Seam<T> load(final String seamId) {
        return Seams.of().<T>byId(seamId).orElseThrow(FailedToLoad::new);
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
