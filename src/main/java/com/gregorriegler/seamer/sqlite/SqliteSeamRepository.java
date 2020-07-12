package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Invokable;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.file.Serializer;
import com.gregorriegler.seamer.kryo.KryoFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
            "create table if not exists seams (id string, invokable blob)"
        );
        serializer = KryoFactory.createSerializer();
    }

    @Override
    public void persist(Seam<T> seam) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.serialize(seam.invokable(), outputStream);

        sqlite.parameterizedCommand("insert into seams (id, invokable) values (?, ?)", seam.id(), outputStream.toByteArray());
    }

    @Override
    public Optional<Seam<T>> byId(String seamId, Invocations invocations) {
        return sqlite.queryBytes("select invokable from seams where id = ?", seamId)
            .map(b -> new Seam<T>(seamId, invokableFrom(b), invocations));
    }

    @SuppressWarnings("unchecked")
    private Invokable<T> invokableFrom(byte[] bytes) {
        return (Invokable<T>) serializer.deserialize(new ByteArrayInputStream(bytes), Invokable.class);
    }
}
