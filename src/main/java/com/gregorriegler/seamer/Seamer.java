package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.FileBasedPersistence;
import com.gregorriegler.seamer.file.FileResetter;

public class Seamer<T> {

    private final SeamRepository<T> seams;
    private final Invocations invocations;

    private Seamer(SeamRepository<T> seams, Invocations invocations) {
        this.seams = seams;
        this.invocations = invocations;
    }

    public static <T> Seam<T> createSeam(String seamId, Invokable<T> invokable) {
        Seamer<T> seamer = create(defaultPersistence());
        return seamer.persist(seamId, invokable);
    }

    public static <T> Seamer<T> create() {
        return create(defaultPersistence());
    }

    public static <T> Seamer<T> create(Persistence<T> persistence) {
        SeamRepository<T> seams = persistence.createSeams();
        Invocations invocations = persistence.createInvocations();
        return new Seamer<>(seams, invocations);
    }


    private Seam<T> persist(String seamId, Invokable<T> invokable) {
        Seam<T> seam = new Seam<>(seamId, invokable, invocations);
        seams.persist(seam);
        return seam;
    }

    public static <T> SeamRecorder<T> customRecordings(String seamId) {
        return Seamer.<T>create(defaultPersistence()).createCustomRecordings(seamId);
    }

    private SeamRecorder<T> createCustomRecordings(String seamId) {
        return seams.byId(seamId, invocations)
            .map(SeamRecorder::new)
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> void verify(String seamId) {
        Seamer.<T>create(defaultPersistence()).verifySeam(seamId);
    }

    private void verifySeam(String seamId) {
        seams.byId(seamId, invocations)
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    private static <T> FileBasedPersistence<T> defaultPersistence() {
        return new FileBasedPersistence<>();
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
