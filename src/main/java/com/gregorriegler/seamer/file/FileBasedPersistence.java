package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.Persistence;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

public class FileBasedPersistence implements Persistence {
    public FileBasedPersistence() {
    }

    @Override
    public SeamRepository createSeams(Serializer serializer) {
        return new FileSeamRepository(serializer);
    }

    @Override
    public Invocations createInvocations(Serializer serializer) {
        return new FileInvocations(serializer);
    }

}