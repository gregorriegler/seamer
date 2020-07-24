package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

public interface Persistence {
    SeamRepository createSeams(Serializer serializer);

    Invocations createInvocations(Serializer serializer);
}
