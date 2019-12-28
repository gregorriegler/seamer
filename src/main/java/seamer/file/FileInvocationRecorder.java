package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationRecorder;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileInvocationRecorder implements InvocationRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationRecorder.class);

    private final Serializer serializer;

    private final String seamId;

    public FileInvocationRecorder(Serializer serializer, String seamId) {
        this.serializer = serializer;
        this.seamId = seamId;
        FileLocation.createSeamDir(seamId);
    }

    @Override
    public void record(Invocation invocation) {
        try {
            FileOutputStream outputStream = new FileOutputStream(FileLocation.invocationsFile(seamId), true);
            serializer.serialize(invocation, outputStream);
        } catch (IOException e) {
            LOG.error("failed to record invocation", e);
        }
    }

}
