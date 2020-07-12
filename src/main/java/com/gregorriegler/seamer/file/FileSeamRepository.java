package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
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
    public void persist(Seam seam) {
        try {
            FileLocation.createSeamDir(seam.id());
            File seamFile = FileLocation.seamFile(seam.id());
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            serializer.serialize(seam.invokable(), new FileOutputStream(seamFile));
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Seam<T>> byId(String seamId, Invocations invocations) {
        return inputStream(seamId)
            .map(stream -> (Invokable<T>) serializer.deserialize(stream, Invokable.class))
            .map(seam -> new Seam<>(seamId, seam, invocations));
    }

    private Optional<FileInputStream> inputStream(String seamId) {
        try {
            return Optional.of(new FileInputStream(FileLocation.seamFile(seamId)));
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
