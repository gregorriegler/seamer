package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRecordingsBuilder;
import com.gregorriegler.seamer.core.SeamVerifier;
import com.gregorriegler.seamer.file.FileInvocations;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoSerializer;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seamer<T> {

    public static final KryoSerializer SERIALIZER = createSerializer();

    private final FileSeamRepository<T> seams;

    public Seamer() {
        this.seams = new FileSeamRepository<>(SERIALIZER);
    }

    public static <T> SeamRecorder<T> create(final String seamId, Invokable<T> invokable) {
        Seam<T> seam = new Seam<>(seamId, invokable);
        return new Seamer<T>().startRecording(seam);
    }

    public static <T> SeamRecordingsBuilder<T> customRecordings(String seamId) {
        return new Seamer<T>().createCustomRecordings(seamId);
    }

    public static <T> void verify(String seamId) {
        new Seamer<T>().verifySeam(seamId);
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    private SeamRecorder<T> startRecording(Seam<T> seam) {
        seams.persist(seam);
        return new SeamRecorder<>(seam, invocations());
    }

    private SeamRecordingsBuilder<T> createCustomRecordings(String seamId) {
        return seams.byId(seamId)
            .map(seam -> new SeamRecordingsBuilder<>(seam, invocations()))
            .orElseThrow(FailedToLoad::new);
    }

    private void verifySeam(String seamId) {
        seams.byId(seamId)
            .map(seam -> new SeamVerifier<>(seam, invocations()))
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    private static FileInvocations invocations() {
        return new FileInvocations(SERIALIZER);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
