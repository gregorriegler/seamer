package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.Call;
import seamer.CallLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileCallLoader implements CallLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileCallLoader.class);

    private final String path;
    private final Kryo kryo = new Kryo();

    public FileCallLoader() {
        this(FileCallRecorder.DEFAULT_DIR);
    }

    public FileCallLoader(String path) {
        this.path = path;
    }

    @Override
    public List<Call> load() {
        try {
            List<Call> calls = Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(file -> loadCallFromFile(file.toFile()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            return calls;
        } catch (IOException e) {
            LOG.error("failed to load calls from " + path, e);
            return Collections.emptyList();
        }
    }

    private Optional<Call> loadCallFromFile(File file) {
        try {
            Input fileInput = new Input(new FileInputStream(file));
            Call call = (Call) kryo.readClassAndObject(fileInput);
            fileInput.close();
            return Optional.of(call);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load call from " + file.getName(), e);
            return Optional.empty();
        }
    }
}
