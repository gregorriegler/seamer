package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.Serializer;

import java.util.Optional;

public class SqliteSeamRepository implements SeamRepository {

    private final Sqlite sqlite;
    private final Serializer serializer;

    public SqliteSeamRepository(String uri, Serializer serializer) {
        sqlite = new Sqlite(uri);
        sqlite.command(
            "create table if not exists seams (id string not null primary key, invokable blob)"
        );
        this.serializer = serializer;
    }

    @Override
    public <T> void persist(Seam<T> seam) {
        sqlite.parameterizedCommand(
            "insert into seams (id, invokable) values (?, ?)",
            seam.id(),
            serializer.toBytesArray(seam.invokable())
        );
    }

    @Override
    public <T> Optional<Seam<T>> byId(String seamId, Invocations invocations) {
        return sqlite.queryBytes("select invokable from seams where id = ?", seamId)
            .map(bytes -> new Seam<T>(seamId, invokableFrom(bytes), invocations));
    }

    @Override
    public void remove(String seamId) {
        sqlite.parameterizedCommand("delete from seams where id = ?", seamId);
    }

    @SuppressWarnings("unchecked")
    private <T> Invokable<T> invokableFrom(byte[] bytes) {
        return serializer.fromBytesArray(bytes, Invokable.class);
    }

}
