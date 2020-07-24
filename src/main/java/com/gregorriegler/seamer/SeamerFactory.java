package com.gregorriegler.seamer;

import com.gregorriegler.seamer.file.FileInvocations;
import com.gregorriegler.seamer.file.FileSeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;

public abstract class SeamerFactory<T> {
    public Seamer<T> createSeamer() {
        FileSeamRepository<T> seams = createSeams();
        FileInvocations invocations = createInvocations();
        return new Seamer<>(seams, invocations);
    }

    protected abstract FileSeamRepository<T> createSeams();

    protected abstract FileInvocations createInvocations();

    protected KryoSerializer serializer() {
        return KryoFactory.createSerializer();
    }
}
