package seamer.core;

import java.io.Serializable;

public class Seamer<T> implements Serializable {

    private final SeamPersister persister;
    private final CallRecorder recorder;

    public Seam<T> seam;

    public Seamer(Seam<T> seam, SeamPersister persister, CallRecorder recorder) {
        this.seam = seam;
        this.persister = persister;
        this.recorder = recorder;
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
        if(persister.isPersisted()) return;
        persister.persist(seam, carrier);
    }

}
