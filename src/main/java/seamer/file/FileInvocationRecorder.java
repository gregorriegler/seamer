package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationRecorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileInvocationRecorder implements InvocationRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationRecorder.class);
    public static final String DEFAULT_DIR = "target/seamer";
    public static final String INVOCATIONS_FILE = "invocations";

    private final Kryo kryo = new Kryo();

    private final String seamId;

    public static File invocationsFile(String seamId) {
        return new File(seamDir(seamId) + File.separator + INVOCATIONS_FILE);
    }

    private static String seamDir(String seamId) {
        return DEFAULT_DIR + File.separator + seamId;
    }

    public FileInvocationRecorder(String seamId) {
        this.seamId = seamId;
        createIfNotExists(seamDir(seamId));
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
            return new Output(new FileOutputStream(invocationsFile(seamId), true));
        } catch (FileNotFoundException e) {
            LOG.error("failed to initialize output", e);
            return new Output();
        }
    }

    private void createIfNotExists(String path) {
        File dir = new File(path);
        dir.mkdirs();
    }
}
