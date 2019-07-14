package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import static seamer.file.FileSeamPersister.SEAM_FILE;
import static seamer.file.FileSeamPersister.persistentFilePath;

public class FileSeamLoader implements SeamLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamLoader.class);

    @Override
    public Optional<Seam> load(String seamId, Object carrier) {
        try {
            Kryo kryo = FileSeamPersister.createKryo(carrier);
            Input fileInput = new Input(new FileInputStream(seamFile(seamId)));
            Object seam = kryo.readClassAndObject(fileInput);
            return Optional.of((Seam) seam);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

    public static File seamFile(String seamId) {
        return new File(persistentFilePath(seamId) + File.separator + SEAM_FILE);
    }

}
