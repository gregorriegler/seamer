package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.SeamResetter;

import java.io.File;

public class FileResetter implements SeamResetter {

    private static final Logger LOG = LoggerFactory.getLogger(FileResetter.class);

    @Override
    public void reset(String seamId) {
        FileLocation.invocationsFile(seamId).delete();
        FileLocation.seamFile(seamId).delete();
        File file = FileLocation.seamDirAsFile(seamId);
        file.delete();
        LOG.info("reset seamer's data for seam id: {}", seamId);
    }

}
