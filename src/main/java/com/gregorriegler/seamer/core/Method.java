package com.gregorriegler.seamer.core;

import java.io.Serializable;

public interface Method<R> extends Serializable {

    R invoke(Object... args);
}
