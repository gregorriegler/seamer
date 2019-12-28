package seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(Seam<T> seam, String seamId);

    Optional<Seam<T>> load(String seamId);
}
