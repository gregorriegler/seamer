package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.Persistence;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;

public class FileBasedPersistence implements Persistence {
    public FileBasedPersistence() {
    }

    @Override
    public SeamRepository createSeams() {
        return new FileSeamRepository(serializer());
    }

    @Override
    public Invocations createInvocations() {
        return new FileInvocations(serializer());
    }

}