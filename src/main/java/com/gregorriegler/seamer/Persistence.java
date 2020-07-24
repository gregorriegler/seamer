package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;

public interface Persistence<T> {

    SeamRepository<T> createSeams();

    Invocations createInvocations();

    default KryoSerializer serializer() {
        return KryoFactory.createSerializer();
    }
}
