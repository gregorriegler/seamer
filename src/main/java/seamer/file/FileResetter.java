package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.SeamResetter;

public class FileResetter implements SeamResetter {

    private static final Logger LOG = LoggerFactory.getLogger(FileResetter.class);

    @Override
    public void reset(String seamId) {
        FileInvocationRecorder.invocationsFile(seamId).delete();
        FileSeamPersister.seamFile(seamId).delete();
        LOG.info("reset seamer's data for seam id: {}", seamId);
    }

}
