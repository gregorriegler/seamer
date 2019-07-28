package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class FileSeamLoader<T> implements SeamLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamLoader.class);

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Seam<T>> load(String seamId, Class carrierClass) {
        try {
            Kryo kryo = FileSeamPersister.createKryo(carrierClass);
            Input fileInput = new Input(new FileInputStream(FileSeamPersister.seamFile(seamId)));
            Object object = kryo.readClassAndObject(fileInput);
            return Optional.of((Seam) object);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
