package seamer.core;

import java.util.Optional;

public interface SeamLoader {

    Optional<Seam> load(String seamId, Object carrier);
}
