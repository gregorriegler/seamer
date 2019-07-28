package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileInvocationLoader implements InvocationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationLoader.class);

    private final Kryo kryo = new Kryo();
    private String seamId;

    public FileInvocationLoader(String seamId) {
        this.seamId = seamId;
    }

    @Override
    public List<Invocation> load() {
        Input input = createInput(this.seamId);
        List<Invocation> invocations = new ArrayList<>();
        while (!input.eof()) {
            Invocation invocation = (Invocation) kryo.readClassAndObject(input);
            invocations.add(invocation);
        }
        return invocations;
    }

    public Input createInput(String seamId) {
        try {
            return new Input(new FileInputStream(FileInvocationRecorder.invocationsFile(seamId)));
        } catch (FileNotFoundException e) {
            LOG.error("failed to initialize input", e);
            return new Input();
        }
    }

}
