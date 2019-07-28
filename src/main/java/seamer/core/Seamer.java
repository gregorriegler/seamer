package seamer.core;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.asList;

public class Seamer<T> implements Serializable {

    private final SeamPersister persister;
    private final InvocationRecorder recorder;
    private final ArgCandidates argCandidates = new ArgCandidates();

    public Seam<T> seam;

    public Seamer(Seam<T> seam, SeamPersister persister, InvocationRecorder recorder) {
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
        recorder.record(Invocation.of(args, result));
        return result;
    }

    public void persist(Class<?> carrierClass) {
        if (persister.isPersisted()) return;
        persister.persist(seam, carrierClass);
    }

    public Seamer<T> addArgCandidates(int i, Object... candidates) {
        argCandidates.addCandidates(i, asList(candidates));
        return this;
    }

    public void shuffleArgsAndExecute() {
        List<Object[]> argCombinations = argCandidates.shuffle();
        for (Object[] args : argCombinations) {
            executeAndRecord(args);
        }
    }

}
