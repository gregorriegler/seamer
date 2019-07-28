package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationLoader;

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

public class FileInvocationLoader implements InvocationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationLoader.class);

    private final String path;
    private final Kryo kryo = new Kryo();

    public FileInvocationLoader(String seamId) {
        this.path = FileInvocationRecorder.pathFromId(seamId);
    }

    @Override
    public List<Invocation> load() {
        try {
            List<Invocation> invocations = Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(file -> loadInvocationFromFile(file.toFile()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            return invocations;
        } catch (IOException e) {
            LOG.error("failed to load invocations from " + path, e);
            return Collections.emptyList();
        }
    }

    private Optional<Invocation> loadInvocationFromFile(File file) {
        try {
            Input fileInput = new Input(new FileInputStream(file));
            Invocation invocation = (Invocation) kryo.readClassAndObject(fileInput);
            fileInput.close();
            return Optional.of(invocation);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load invocation from " + file.getName(), e);
            return Optional.empty();
        }
    }
}
