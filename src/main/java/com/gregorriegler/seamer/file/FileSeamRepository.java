package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.ProxySuture;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.SeamWithId;
import com.gregorriegler.seamer.core.Suture;
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
    public void persist(SeamWithId seamWithId) {
        try {
            FileLocation.createSeamDir(seamWithId.id());
            File seamFile = FileLocation.seamFile(seamWithId.id());
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            serializer.serialize(seamWithId.seam(), new FileOutputStream(seamFile));
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<SeamWithId<T>> byId(String seamId) {
        return inputStream(seamId).map(stream -> (Suture<T>) serializer.deserialize(stream, Suture.class)).map(seam -> new SeamWithId<>(seamId, seam));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<ProxySuture<T>> proxyById(String seamId) {
        return inputStream(seamId).map(stream -> serializer.deserialize(stream, ProxySuture.class));
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
