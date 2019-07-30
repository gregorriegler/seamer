package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationRecorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileInvocationRecorder implements InvocationRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationRecorder.class);

    private final Kryo kryo = new Kryo();

    private final String seamId;

    public FileInvocationRecorder(String seamId) {
        this.seamId = seamId;
        FileLocation.createSeamDir(seamId);
    }

    @Override
    public void record(Invocation invocation) {
        Output fileOutput = createOutput(seamId);
        kryo.writeClassAndObject(fileOutput, invocation);
        fileOutput.flush();
        fileOutput.close();
    }

    public Output createOutput(String seamId) {
        try {
            return new Output(new FileOutputStream(FileLocation.invocationsFile(seamId), true));
        } catch (FileNotFoundException e) {
            LOG.error("failed to initialize output", e);
            return new Output();
        }
    }

}
