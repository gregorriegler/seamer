package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Serializer;

import java.util.List;
import java.util.stream.Collectors;

public class SqliteInvocations implements Invocations {

    private final Sqlite sqlite;
    private final Serializer serializer;

    public SqliteInvocations(String uri, Serializer serializer) {
        sqlite = new Sqlite(uri);
        sqlite.command("create table if not exists invocations (seam_id string not null, invocation blob)");
        this.serializer = serializer;
    }

    @Override
    public void record(String seamId, Invocation invocation) {
        sqlite.parameterizedCommand("insert into invocations (seam_id, invocation) values (?, ?)", seamId, serializer.toBytesArray(invocation));
    }

    @Override
    public List<Invocation> getAll(String seamId) {
        List<Invocation> result = sqlite.queryListOfBytes("select invocation from invocations where seam_id = ?", seamId)
            .stream()
            .map(bytes -> serializer.fromBytesArray(bytes, Invocation.class))
            .collect(Collectors.toList());
        return result;
    }

    @Override
    public void remove(String seamId) {
        sqlite.parameterizedCommand("delete from invocations where seam_id = ?", seamId);
    }
}
