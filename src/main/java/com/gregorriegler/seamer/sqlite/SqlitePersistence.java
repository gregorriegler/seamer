package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Persistence;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;

public class SqlitePersistence implements Persistence {
    @Override
    public SeamRepository createSeams() {
        return new SqliteSeamRepository();
    }

    @Override
    public Invocations createInvocations() {
        return new SqliteInvocations();
    }
}
