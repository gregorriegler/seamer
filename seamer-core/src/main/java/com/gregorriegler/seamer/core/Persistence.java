package com.gregorriegler.seamer.core;

public interface Persistence {
    SeamRepository createSeams(Serializer serializer);

    Invocations createInvocations(Serializer serializer);
}
