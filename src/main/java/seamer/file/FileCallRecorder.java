package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.Call;
import seamer.CallRecorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileCallRecorder implements CallRecorder {

    public static final String DEFAULT_DIR = "target/seamer";
    public static final String CALLS_DIR = "calls";
    private static final Logger LOG = LoggerFactory.getLogger(FileCallRecorder.class);

    private final String path;
    private final Kryo kryo = new Kryo();

    public FileCallRecorder(String seamId) {
        this.path = DEFAULT_DIR + File.separator + seamId + File.separator + CALLS_DIR;
        createIfNotExists(this.path);
    }

    @Override
    public void record(Call call) {
        File file = nextFile();
        writeCallToFile(call, file);
    }

    public void writeCallToFile(Call call, File file) {
        try {
            Output fileOutput = new Output(new FileOutputStream(file));
            kryo.writeClassAndObject(fileOutput, call);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to record call", e);
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
