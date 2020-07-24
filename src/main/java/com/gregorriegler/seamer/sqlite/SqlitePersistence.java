package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Persistence;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;

public class SqlitePersistence<T> implements Persistence<T> {
    @Override
    public SeamRepository<T> createSeams() {
        return new SqliteSeamRepository<>();
    }

    @Override
    public Invocations createInvocations() {
        return new SqliteInvocations();
    }
}
