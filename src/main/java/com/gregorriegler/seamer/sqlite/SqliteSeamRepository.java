package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.Serializer;
import com.gregorriegler.seamer.kryo.KryoFactory;

import java.util.Optional;

public class SqliteSeamRepository<T> implements SeamRepository<T> {

    private final Sqlite sqlite;
    private final Serializer serializer;

    public SqliteSeamRepository() {
        this("jdbc:sqlite::memory:");
    }

    public SqliteSeamRepository(String url) {
        sqlite = new Sqlite(url);
        sqlite.command(
            "create table if not exists seams (id string not null primary key, invokable blob)"
        );
        serializer = KryoFactory.createSerializer();
    }

    @Override
    public void persist(Seam<T> seam) {
        sqlite.parameterizedCommand(
            "insert into seams (id, invokable) values (?, ?)",
            seam.id(),
            serializer.toBytesArray(seam.invokable())
        );
    }

    @Override
    public Optional<Seam<T>> byId(String seamId, Invocations invocations) {
        return sqlite.queryBytes("select invokable from seams where id = ?", seamId)
            .map(bytes -> new Seam<>(seamId, invokableFrom(bytes), invocations));
    }

    @SuppressWarnings("unchecked")
    private Invokable<T> invokableFrom(byte[] bytes) {
        return serializer.fromBytesArray(bytes, Invokable.class);
    }

}
