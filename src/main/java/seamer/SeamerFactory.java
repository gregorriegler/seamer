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

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Class carrierClass, final String seamId) {
        Seamer<T> seamer = create(seam, seamId);
        seamer.persist(carrierClass);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, final String seamId) {
        return new Seamer<T>(
            seam,
            new FileSeamPersister(seamId),
            new FileInvocationRecorder(seamId),
            new FileInvocationLoader(seamId)
        );
    }

    public static <T> Seamer<T> load(final String seamId, Class carrierClass) {
        return new FileSeamLoader<T>(seamId).load(carrierClass)
            .map(s -> create(s, seamId))
            .orElseThrow(() -> new FailedToLoad());
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
