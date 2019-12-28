package seamer.core;

public interface SeamPersister {
    void persist(Seam<?> seam, String seamId);

    boolean isPersisted(String seamId);
}
