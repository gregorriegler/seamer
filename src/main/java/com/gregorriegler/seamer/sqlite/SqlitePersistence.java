package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Persistence;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

public class SqlitePersistence implements Persistence {
    @Override
    public SeamRepository createSeams(Serializer serializer) {
        return new SqliteSeamRepository(serializer);
    }

    @Override
    public Invocations createInvocations(Serializer serializer) {
        return new SqliteInvocations(serializer);
    }
}
