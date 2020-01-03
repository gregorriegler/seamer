package com.gregorriegler.seamer.core;

import java.io.Serializable;

public interface Signature<R> extends Serializable {

    R invoke(Object... args);
}
