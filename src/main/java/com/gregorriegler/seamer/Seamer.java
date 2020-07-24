package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer<T> {

    private final SeamRepository<T> seams;
    private final Invocations invocations;

    public Seamer(SeamRepository<T> seams, Invocations invocations) {
        this.seams = seams;
        this.invocations = invocations;
    }

    public static <T> Seam<T> create(String seamId, Invokable<T> invokable) {
        return Seamer.<T>defaultFactory().createSeamer().persist(seamId, invokable);
    }


    private Seam<T> persist(String seamId, Invokable<T> invokable) {
        Seam<T> seam = new Seam<>(seamId, invokable, invocations);
        seams.persist(seam);
        return seam;
    }

    public static <T> SeamRecorder<T> customRecordings(String seamId) {
        return Seamer.<T>defaultFactory().createSeamer().createCustomRecordings(seamId);
    }

    private SeamRecorder<T> createCustomRecordings(String seamId) {
        return seams.byId(seamId, invocations)
            .map(SeamRecorder::new)
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> void verify(String seamId) {
        defaultFactory().createSeamer().verifySeam(seamId);
    }

    private void verifySeam(String seamId) {
        seams.byId(seamId, invocations)
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    private static <T> FileSeamerFactory<T> defaultFactory() {
        return new FileSeamerFactory<>();
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
