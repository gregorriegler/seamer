package seamer.core;

import java.util.Optional;

public interface SeamRepository<T> {

    void persist(Signature<T> signature, String seamId);

    Optional<Signature<T>> load(String seamId);
}
