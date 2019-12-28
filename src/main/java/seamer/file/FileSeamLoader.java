package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamLoader;
import seamer.kryo.KryoFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class FileSeamLoader<T> implements SeamLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamLoader.class);
    private final Kryo kryo;

    public FileSeamLoader(Class<?> carrierClass) {
        this.kryo = KryoFactory.createKryo(carrierClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Seam<T>> load(String seamId) {
        try {
            Input fileInput = new Input(new FileInputStream(FileLocation.seamFile(seamId)));
            Object object = kryo.readClassAndObject(fileInput);
            return Optional.of((Seam<T>) object);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
