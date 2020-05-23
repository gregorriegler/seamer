package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.core.SeamRecordingsBuilder;
import com.gregorriegler.seamer.core.SeamVerifier;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileMethodRepository;
import com.gregorriegler.seamer.file.FileResetter;
import com.gregorriegler.seamer.kryo.KryoSerializer;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seamer<T> {

    public static final KryoSerializer SERIALIZER = createSerializer();

    private final FileMethodRepository<T> methods;

    public Seamer() {
        this.methods = new FileMethodRepository<>(SERIALIZER);
    }

    public static <T> SeamRecorder<T> create(final String seamId, Method<T> method) {
        return new Seamer<T>().createSeam(seamId, method);
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

    private SeamRecorder<T> createSeam(final String seamId, Method<T> method) {
        methods.persist(seamId, method);
        return new SeamRecorder<>(method, invocations(seamId));
    }

    private SeamRecordingsBuilder<T> createCustomRecordings(String seamId) {
        return methods.byId(seamId)
            .map(method -> new SeamRecordingsBuilder<>(method, invocations(seamId)))
            .orElseThrow(FailedToLoad::new);
    }

    private void verifySeam(String seamId) {
        methods.byId(seamId)
            .map(method -> new SeamVerifier<>(method, invocations(seamId)))
            .orElseThrow(FailedToLoad::new)
            .verify();
    }

    private static FileInvocationRepository invocations(String seamId) {
        return new FileInvocationRepository(seamId, SERIALIZER);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
