package com.gregorriegler.seamer;

import com.gregorriegler.seamer.file.FileInvocations;
import com.gregorriegler.seamer.file.FileSeamRepository;

public class FileSeamerFactory<T> extends SeamerFactory<T> {
    public FileSeamerFactory() {
    }

    @Override
    public FileSeamRepository<T> createSeams() {
        return new FileSeamRepository<>(serializer());
    }

    @Override
    public FileInvocations createInvocations() {
        return new FileInvocations(serializer());
    }

}