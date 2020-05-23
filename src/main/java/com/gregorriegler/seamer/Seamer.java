package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer {

    public static <T> SeamRecorder<T> create(final String seamId, Method<T> method) {
        return new Seams<T>().addToRecord(seamId, method);
    }

    public static <T> Seam<T> load(final String seamId) {
        return new Seams<T>().byId(seamId).orElseThrow(FailedToLoad::new);
    }

    public static void verify(String seamId) {
        new Seams<>().verifierById(seamId)
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
