package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileInvocations implements Invocations {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocations.class);

    private final Serializer serializer;

    public FileInvocations(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void record(String seamId, Invocation invocation) {
        try {
            FileLocation.createSeamDir(seamId);
            FileOutputStream outputStream = new FileOutputStream(FileLocation.invocationsFile(seamId), true);
            serializer.serialize(invocation, outputStream);
        } catch (IOException e) {
            LOG.error("failed to record invocation", e);
        }
    }

    @Override
    public List<Invocation> getAll(String seamId) {
        try {
            FileInputStream inputStream = new FileInputStream(FileLocation.invocationsFile(seamId));
            return serializer.deserializeList(inputStream, Invocation.class);
        } catch (FileNotFoundException e) {
            LOG.error("found no recorded invocations for seam '{}'", seamId, e);
            return Collections.emptyList();
        }
    }

}
