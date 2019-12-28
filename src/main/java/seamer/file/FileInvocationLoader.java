package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.InvocationLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class FileInvocationLoader implements InvocationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileInvocationLoader.class);

    private final Serializer serializer;
    private String seamId;

    public FileInvocationLoader(Serializer serializer, String seamId) {
        this.serializer = serializer;
        this.seamId = seamId;
    }

    @Override
    public List<Invocation> load() {
        try {
            FileInputStream inputStream = new FileInputStream(FileLocation.invocationsFile(this.seamId));
            return serializer.deserializeInvocations(inputStream);
        } catch (FileNotFoundException e) {
            LOG.error("found no recorded invocations for seam '{}'", this.seamId, e);
            return Collections.emptyList();
        }
    }

}
