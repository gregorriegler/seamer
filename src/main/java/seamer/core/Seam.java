package seamer.core;

import java.io.Serializable;

public interface Seam<R> extends Serializable {

    R invoke(Object... args);
}
