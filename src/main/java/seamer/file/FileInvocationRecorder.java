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

    public static final String DEFAULT_DIR = "target/seamer";
    public static final String INVOCATIONS_DIR = "invocations";
    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationRecorder.class);

    private final String path;
    private final Kryo kryo = new Kryo();

    public FileInvocationRecorder(String seamId) {
        this.path = pathFromId(seamId);
        createIfNotExists(this.path);
    }

    public static String pathFromId(String seamId) {
        return DEFAULT_DIR + File.separator + seamId + File.separator + INVOCATIONS_DIR;
    }

    @Override
    public void record(Invocation invocation) {
        File file = nextFile();
        writeInvocationToFile(invocation, file);
    }

    public void writeInvocationToFile(Invocation invocation, File file) {
        try {
            Output fileOutput = new Output(new FileOutputStream(file));
            kryo.writeClassAndObject(fileOutput, invocation);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to record invocation", e);
        }
    }

    private File nextFile() {
        int n = 0;
        File file;
        do {
            file = fileFrom(n);
            n++;
        } while (file.exists());
        return file;
    }

    private File fileFrom(int n) {
        return new File(path + File.separator + String.format("%05d", n));
    }

    private void createIfNotExists(String path) {
        File dir = new File(path);
        dir.mkdirs();
    }
}
