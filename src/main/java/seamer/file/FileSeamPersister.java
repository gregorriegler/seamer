package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamPersister;
import seamer.kryo.KryoFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileSeamPersister implements SeamPersister {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamPersister.class);
    private final String seamId;
    private final Kryo kryo;

    public FileSeamPersister(Class<?> carrierClass, String seamId) {
        this.seamId = seamId;
        this.kryo = KryoFactory.createKryo(carrierClass);
    }

    @Override
    public void persist(Seam<?> seam, Class<?> carrierClass) {
        try {
            FileLocation.createSeamDir(seamId);
            File seamFile = FileLocation.seamFile(seamId);
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            Output fileOutput = new Output(new FileOutputStream(seamFile));
            kryo.writeClassAndObject(fileOutput, seam);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    @Override
    public boolean isPersisted() {
        return FileLocation.seamFile(seamId).exists();
    }

}