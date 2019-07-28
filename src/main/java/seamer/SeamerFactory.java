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

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Object carrier) {
        Seamer<T> seamer = create(seam, carrier);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, Object carrier) {
        String seamId = idOf(carrier);

        return new Seamer<>(
            seam,
            new FileSeamPersister(seamId),
            new FileInvocationRecorder(seamId)
        );
    }

    public static <T> Seamer<T> load(Object carrier) {
        String seamId = idOf(carrier);

        return new FileSeamLoader<T>().load(seamId, carrier)
            .map(s -> create(s, carrier))
            .orElseThrow(() -> new FailedToLoad());
    }

    public static List<Invocation> loadInvocations(Object carrier) {
        return new FileInvocationLoader(SeamerFactory.idOf(carrier)).load();
    }

    public static void reset() {
        new FileResetter().resetAll();
    }

    public static String idOf(Object carrier) {
        return carrier.getClass().getName();
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
