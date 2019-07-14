package seamer.core;

public interface SeamPersister {
    void persist(String seamId, Seam seam, Object carrier);
}
