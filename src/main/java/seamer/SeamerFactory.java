package seamer;

import seamer.core.Invocation;
import seamer.core.Seam;
import seamer.core.Seamer;
import seamer.file.FileInvocationLoader;
import seamer.file.FileInvocationRecorder;
import seamer.file.FileResetter;
import seamer.file.FileSeamLoader;
import seamer.file.FileSeamPersister;

import java.util.List;

public class SeamerFactory {

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Class<?> carrierClass, final String seamId) {
        Seamer<T> seamer = create(seam, carrierClass, seamId);
        seamer.persist(seamId);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, Class<?> carrierClass, final String seamId) {
        return new Seamer<>(
            seam,
            new FileSeamPersister(carrierClass),
            new FileInvocationRecorder(seamId),
            new FileInvocationLoader(seamId)
        );
    }

    public static <T> Seamer<T> load(Class<?> carrierClass, final String seamId) {
        final FileSeamLoader<T> fileSeamLoader = new FileSeamLoader<>(carrierClass);
        return fileSeamLoader.load(seamId)
            .map(s -> create(s, carrierClass, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static List<Invocation> loadInvocations(final String seamId) {
        return new FileInvocationLoader(seamId).load();
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
