package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Optional;

public class FileSeamRepository<T> implements SeamRepository<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamRepository.class);
    private final Serializer serializer;

    public FileSeamRepository(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void persist(Signature<T> signature, String seamId) {
        try {
            FileLocation.createSeamDir(seamId);
            File seamFile = FileLocation.seamFile(seamId);
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            serializer.serialize(signature, new FileOutputStream(seamFile));
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Signature<T>> load(String seamId) {
        try {
            Object deserialize = serializer.deserialize(new FileInputStream(FileLocation.seamFile(seamId)));
            return Optional.of((Signature<T>) deserialize);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
