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

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Object carrier, final String seamId) {
        Seamer<T> seamer = create(seam, seamId);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, final String seamId) {
        return new Seamer<>(
            seam,
            new FileSeamPersister(seamId),
            new FileInvocationRecorder(seamId)
        );
    }

    public static <T> Seamer<T> load(Object carrier, final String seamId) {
        return new FileSeamLoader<T>().load(seamId, carrier)
            .map(s -> create(s, seamId))
            .orElseThrow(() -> new FailedToLoad());
    }

    public static List<Invocation> loadInvocations(final String seamId) {
        return new FileInvocationLoader(seamId).load();
    }

    public static void reset() {
        new FileResetter().resetAll();
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
