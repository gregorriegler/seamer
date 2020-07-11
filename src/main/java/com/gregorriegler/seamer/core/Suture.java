package com.gregorriegler.seamer.core;

import java.io.Serializable;

public interface Suture<R> extends Serializable {

    R invoke(Object... args);
}
