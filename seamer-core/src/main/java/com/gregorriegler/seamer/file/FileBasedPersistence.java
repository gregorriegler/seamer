package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Persistence;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

public class FileBasedPersistence implements Persistence {

    private final String basePath;

    public FileBasedPersistence() {
        this(FileLocation.basePath());
    }

    public FileBasedPersistence(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public SeamRepository createSeams(Serializer serializer) {
        return new FileSeamRepository(basePath, serializer);
    }

    @Override
    public Invocations createInvocations(Serializer serializer) {
        return new FileInvocations(basePath, serializer);
    }
}