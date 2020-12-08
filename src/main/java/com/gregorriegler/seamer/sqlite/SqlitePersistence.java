package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Persistence;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

public class SqlitePersistence implements Persistence {
    private final String uri;

    public static SqlitePersistence atTmp() {
        return new SqlitePersistence("jdbc:sqlite:/tmp/seamer");
    }

    public SqlitePersistence() {
        this("jdbc:sqlite::memory:");
    }

    public SqlitePersistence(String uri) {
        this.uri = uri;
    }

    @Override
    public SeamRepository createSeams(Serializer serializer) {
        return new SqliteSeamRepository(uri, serializer);
    }

    @Override
    public Invocations createInvocations(Serializer serializer) {
        return new SqliteInvocations(uri, serializer);
    }
}
