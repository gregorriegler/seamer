package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Persistence;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.FileBasedPersistence;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;

public class Seamer {

    public static <T> Seam<T> createSeam(String seamId, Invokable<T> invokable) {
        return create().persist(seamId, invokable);
    }

    public static <T> Seam<T> createSeam(String basePath, String seamId, Invokable<T> invokable) {
        return create(basePath).persist(seamId, invokable);
    }

    public static <T> SeamRecorder<T> customRecordings(String seamId) {
        return create().createCustomRecordings(seamId);
    }

    public static <T> SeamRecorder<T> customRecordings(String basePath, String seamId) {
        return create(basePath).createCustomRecordings(seamId);
    }

    public static Seamer create() {
        return create(new FileBasedPersistence());
    }

    public static Seamer create(String basePath) {
        return create(new FileBasedPersistence(basePath));
    }

    public static Seamer create(Persistence persistence) {
        KryoSerializer serializer = KryoFactory.createSerializer();
        SeamRepository seams = persistence.createSeams(serializer);
        Invocations invocations = persistence.createInvocations(serializer);
        return new Seamer(seams, invocations);
    }

    private final SeamRepository seams;

    private final Invocations invocations;

    private Seamer(SeamRepository seams, Invocations invocations) {
        this.seams = seams;
        this.invocations = invocations;
    }

    public void reset(String seamId) {
        seams.remove(seamId);
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

    public void verify(String seamId) {
        seams.byId(seamId, invocations)
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
