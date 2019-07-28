package seamer.core;

public interface SeamPersister {
    void persist(Seam seam, Class carrierClass);

    boolean isPersisted();
}
