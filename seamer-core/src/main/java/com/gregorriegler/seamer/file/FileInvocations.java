package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.gregorriegler.seamer.file.FileLocation.createSeamDir;
import static com.gregorriegler.seamer.file.FileLocation.invocationsFile;

public class FileInvocations implements Invocations {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocations.class);

    private final Serializer serializer;
    private final String basePath;

    public FileInvocations(String basePath, Serializer serializer) {
        this.serializer = serializer;
        this.basePath = basePath;
    }

    @Override
    public void record(String seamId, Invocation invocation) {
        try {
            createSeamDir(basePath, seamId);
            FileOutputStream outputStream = new FileOutputStream(invocationsFile(basePath, seamId), true);
            serializer.serialize(invocation, outputStream);
        } catch (IOException e) {
            LOG.error("failed to record invocation", e);
        }
    }

    @Override
    public List<Invocation> getAll(String seamId) {
        File file = invocationsFile(basePath, seamId);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        return loadInvocations(seamId, file);
    }

    private List<Invocation> loadInvocations(String seamId, File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return serializer.deserializeList(inputStream, Invocation.class);
        } catch (FileNotFoundException e) {
            LOG.error("found no recorded invocations for seam '{}'", seamId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void remove(String seamId) {
        invocationsFile(basePath, seamId).delete();
    }
}
