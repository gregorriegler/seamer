package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Optional;

public class FileSeamRepository implements SeamRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamRepository.class);
    private final Serializer serializer;
    private final String basePath;

    public FileSeamRepository(Serializer serializer) {
        this(FileLocation.DEFAULT_BASE_PATH, serializer);
    }

    public FileSeamRepository(String basePath, Serializer serializer) {
        this.basePath = basePath;
        this.serializer = serializer;
    }

    @Override
    public <T> void persist(Seam<T> seam) {
        try {
            FileLocation.createSeamDir(basePath, seam.id());
            File seamFile = FileLocation.seamFile(basePath, seam.id());
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            serializer.serialize(seam.invokable(), new FileOutputStream(seamFile));
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<Seam<T>> byId(String seamId, Invocations invocations) {
        return inputStream(seamId)
            .map(stream -> (Invokable<T>) serializer.deserialize(stream, Invokable.class))
            .map(seam -> new Seam<>(seamId, seam, invocations));
    }

    @Override
    public void remove(String seamId) {
        FileLocation.invocationsFile(basePath, seamId).delete();
        FileLocation.seamFile(basePath, seamId).delete();
        FileLocation.seamDirAsFile(basePath, seamId).delete();
        LOG.info("removed seam of id: {}", seamId);
    }

    private Optional<FileInputStream> inputStream(String seamId) {
        try {
            return Optional.of(new FileInputStream(FileLocation.seamFile(basePath, seamId)));
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
