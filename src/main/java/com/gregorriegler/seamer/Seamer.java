package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.FileBasedPersistence;

public class Seamer {

    public static <T> Seam<T> createSeam(String seamId, Invokable<T> invokable) {
        return create().persist(seamId, invokable);
    }

    public static void verify(String seamId) {
        create().verifySeam(seamId);
    }

    public static <T> SeamRecorder<T> customRecordings(String seamId) {
        return create().createCustomRecordings(seamId);
    }

    public static void reset(String seamId) {
        create().seams.remove(seamId);
    }

    public static Seamer create() {
        return create(defaultPersistence());
    }

    public static Seamer create(Persistence persistence) {
        SeamRepository seams = persistence.createSeams();
        Invocations invocations = persistence.createInvocations();
        return new Seamer(seams, invocations);
    }

    private static FileBasedPersistence defaultPersistence() {
        return new FileBasedPersistence();
    }

    private final SeamRepository seams;

    private final Invocations invocations;

    private Seamer(SeamRepository seams, Invocations invocations) {
        this.seams = seams;
        this.invocations = invocations;
    }

    private <T> Seam<T> persist(String seamId, Invokable<T> invokable) {
        Seam<T> seam = new Seam<>(seamId, invokable, invocations);
        seams.persist(seam);
        return seam;
    }

    private <T> SeamRecorder<T> createCustomRecordings(String seamId) {
        return seams.<T>byId(seamId, invocations)
            .map(SeamRecorder::new)
            .orElseThrow(FailedToLoad::new);
    }

    private void verifySeam(String seamId) {
        seams.byId(seamId, invocations)
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
