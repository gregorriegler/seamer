package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRecorder;
import com.gregorriegler.seamer.file.FileInvocationRepository;
import com.gregorriegler.seamer.file.FileMethodRepository;
import com.gregorriegler.seamer.kryo.KryoSerializer;

import java.util.Optional;

import static com.gregorriegler.seamer.kryo.KryoFactory.createSerializer;

public class Seams<T> {

    public static final KryoSerializer SERIALIZER = createSerializer();
    private final FileMethodRepository<T> methods;

    public Seams() {
        methods = new FileMethodRepository<>(SERIALIZER);
    }

    public SeamRecorder<T> addToRecord(String seamId, Method<T> method) {
        methods.persist(seamId, method);
        return new SeamRecorder<>(method, new FileInvocationRepository(seamId, SERIALIZER));
    }

    public Optional<Seam<T>> byId(String seamId) {
        return methods.byId(seamId)
            .map(method -> new Seam<>(method, new FileInvocationRepository(seamId, SERIALIZER)));
    }

}
