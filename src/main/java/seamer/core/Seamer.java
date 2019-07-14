package seamer.core;

import seamer.file.FileCallRecorder;
import seamer.file.FileSeamLoader;
import seamer.file.FileSeamPersister;

import java.io.Serializable;

public class Seamer<T> implements Serializable {

    private final SeamPersister persister = new FileSeamPersister();

    public Seam<T> seam;
    private CallRecorder recorder;

    public static <T> Seamer<T> create(Seam<T> seam, Object carrier) {
        return new Seamer<>(seam, carrier);
    }

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Object carrier) {
        Seamer<T> seamer = create(seam, carrier);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> load(Object carrier) {
        FileSeamLoader loader = new FileSeamLoader();
        Seam load = loader.load(FileSeamPersister.idOf(carrier), carrier);
        return create(load, carrier);
    }

    private Seamer(Seam<T> seam, Object carrier) {
        this.seam = seam;
        this.recorder = new FileCallRecorder(idOf(carrier));
    }

    public T execute(Object... args) {
        T result = seam.apply(args);
        return result;
    }

    public T executeAndRecord(Object... args) {
        T result = execute(args);
        recorder.record(Call.of(args, result));
        return result;
    }

    public void persist(Object carrier) {
        persister.persist(FileSeamPersister.idOf(carrier), seam, carrier);
    }

    public static String idOf(Object carrier) {
        return carrier.getClass().getName();
    }
}
