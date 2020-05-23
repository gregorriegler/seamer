package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRecordingsBuilder;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer {

    public static <T> SeamRecorder<T> create(final String seamId, Method<T> method) {
        return new Seams<T>().create(seamId, method);
    }

    public static <T> SeamRecordingsBuilder<T> customRecordings(String seamId) {
        return new Seams<T>().recordingsBuilderById(seamId)
            .orElseThrow(FailedToLoad::new);
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
