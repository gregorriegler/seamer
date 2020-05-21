package com.gregorriegler.seamer.file;

import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import com.gregorriegler.seamer.core.Method;
import com.gregorriegler.seamer.core.MethodRepository;
import com.gregorriegler.seamer.core.ProxyMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Optional;

public class FileMethodRepository<T> implements MethodRepository<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FileMethodRepository.class);
    private final Serializer serializer;

    public FileMethodRepository(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void persist(String seamId, Method<T> method) {
        try {
            FileLocation.createSeamDir(seamId);
            File seamFile = FileLocation.seamFile(seamId);
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            serializer.serialize(method, new FileOutputStream(seamFile));
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Method<T>> byId(String seamId) {
        try {
            Object deserialize = serializer.deserialize(
                new FileInputStream(FileLocation.seamFile(seamId)),
                ClosureSerializer.Closure.class
            );
            return Optional.of((Method<T>) deserialize);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProxyMethod<T>> proxyById(String seamId) {
        try {
            ProxyMethod deserialize = serializer.deserialize(
                new FileInputStream(FileLocation.seamFile(seamId)),
                ProxyMethod.class
            );
            return Optional.of(deserialize);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
