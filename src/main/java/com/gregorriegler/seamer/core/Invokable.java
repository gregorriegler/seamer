package com.gregorriegler.seamer.core;

import java.io.Serializable;

public interface Invokable<R> extends Serializable {

    R invoke(Object... args);
}
