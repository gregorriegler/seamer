package seamer.file;

import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamPersister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileSeamPersister implements SeamPersister {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamPersister.class);
    private final Serializer serializer;

    public FileSeamPersister(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void persist(Seam<?> seam, String seamId) {
        try {
            FileLocation.createSeamDir(seamId);
            File seamFile = FileLocation.seamFile(seamId);
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            Output fileOutput = serializer.serialize(seam, new FileOutputStream(seamFile));
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @Override
    public boolean isPersisted(String seamId) {
        return FileLocation.seamFile(seamId).exists();
    }

}