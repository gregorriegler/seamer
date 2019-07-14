package seamer;

import seamer.core.Seam;
import seamer.core.Seamer;
import seamer.file.FileCallRecorder;
import seamer.file.FileSeamLoader;
import seamer.file.FileSeamPersister;

public class SeamerFactory {

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Object carrier) {
        Seamer<T> seamer = create(seam, carrier);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, Object carrier) {
        return new Seamer<>(
            seam,
            new FileSeamPersister(),
            new FileCallRecorder(Seamer.idOf(carrier))
        );
    }

    public static <T> Seamer<T> load(Object carrier) {
        FileSeamLoader loader = new FileSeamLoader();
        return (Seamer<T>) loader.load(Seamer.idOf(carrier), carrier)
            .map(s -> create(s, carrier))
            .orElseThrow(() -> new Seamer.FailedToLoad());
    }
}
