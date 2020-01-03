package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.SeamResetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileResetter implements SeamResetter {

    private static final Logger LOG = LoggerFactory.getLogger(FileResetter.class);

    @Override
    public void reset(String seamId) {
        FileLocation.invocationsFile(seamId).delete();
        FileLocation.seamFile(seamId).delete();
        FileLocation.seamDirAsFile(seamId).delete();
        LOG.info("reset seamer's data for seam id: {}", seamId);
    }

}
