package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRecordingsBuilder;
import com.gregorriegler.seamer.core.SeamVerifier;
import com.gregorriegler.seamer.core.SeamWithId;
import com.gregorriegler.seamer.core.Suture;
import com.gregorriegler.seamer.file.FileInvocationRepository;
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

    public static <T> SeamRecorder<T> create(final String seamId, Suture<T> suture) {
        SeamWithId<T> seamWithId = new SeamWithId<>(seamId, suture);
        return new Seamer<T>().startRecording(seamWithId);
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

    private SeamRecorder<T> startRecording(SeamWithId<T> seamWithId) {
        seams.persist(seamWithId);
        return new SeamRecorder<>(seamWithId, invocations());
    }

    private SeamRecordingsBuilder<T> createCustomRecordings(String seamId) {
        return seams.byId(seamId)
            .map(seamWithId -> new SeamRecordingsBuilder<>(seamWithId, invocations()))
            .orElseThrow(FailedToLoad::new);
    }

    private void verifySeam(String seamId) {
        seams.byId(seamId)
            .map(seamWithId -> new SeamVerifier<T>(seamWithId, invocations()))
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    private static FileInvocationRepository invocations() {
        return new FileInvocationRepository(SERIALIZER);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
