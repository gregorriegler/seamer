package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamLoader;
import seamer.kryo.KryoSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class FileSeamLoader<T> implements SeamLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamLoader.class);
    private final Serializer serializer;

    public FileSeamLoader(Class<?> carrierClass) {
        serializer = new KryoSerializer(carrierClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Seam<T>> load(String seamId) {
        try {
            Object deserialize = serializer.deserialize(new FileInputStream(FileLocation.seamFile(seamId)));
            return Optional.of((Seam<T>) deserialize);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return Optional.empty();
        }
    }

}
