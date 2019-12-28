package seamer;

import seamer.core.Invocation;
import seamer.core.Seam;
import seamer.core.Seamer;
import seamer.file.FileInvocationRepository;
import seamer.file.FileResetter;
import seamer.file.FileSeamLoader;
import seamer.file.FileSeamPersister;
import seamer.kryo.KryoSerializer;

import java.util.List;

public class SeamerFactory {

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Class<?> carrierClass, final String seamId) {
        Seamer<T> seamer = create(seam, seamId);
        new FileSeamPersister(new KryoSerializer(carrierClass)).persist(seam, seamId);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, final String seamId) {
        return new Seamer<>(
            seam,
            new FileInvocationRepository(new KryoSerializer(), seamId)
        );
    }

    public static <T> Seamer<T> load(Class<?> carrierClass, final String seamId) {
        return new FileSeamLoader<T>(new KryoSerializer(carrierClass))
            .load(seamId)
            .map(seam -> create(seam, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static List<Invocation> loadInvocations(final String seamId) {
        return new FileInvocationRepository(new KryoSerializer(), seamId).getAll();
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
