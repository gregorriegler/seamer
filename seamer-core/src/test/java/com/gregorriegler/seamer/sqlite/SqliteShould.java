package com.gregorriegler.seamer.sqlite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteShould {

    private final String seamId = "irrelevant";
    private final byte[] seam = "hello".getBytes();
    private Sqlite sqlite;

    @BeforeEach
    void setUp() {
        sqlite = new Sqlite("jdbc:sqlite::memory:");
        sqlite.command(
            "drop table if exists seams",
            "create table seams (id string, invokable blob)",
            "drop table if exists invocations",
            "create table invocations (seam_id string, invocation blob)"
        );
    }

    @AfterEach
    void tearDown() {
        sqlite.close();
    }

    @Test
    void persist_and_retrieve_data() {
        sqlite.parameterizedCommand("insert into seams values(?, ?)", seamId, seam);

        byte[] result = sqlite.queryBytes("select invokable from seams where id = ?", seamId).get();

        assertThat(result).isEqualTo(seam);
    }

    @Test
    void not_find_non_existent_data() {
        Optional<byte[]> result = sqlite.queryBytes("select invokable from seams where id = ?", seamId);

        assertThat(result).isEmpty();
    }

    @Test
    void find_list_of_data() {
        sqlite.parameterizedCommand("insert into invocations values(?, ?)", seamId, "hello".getBytes());
        sqlite.parameterizedCommand("insert into invocations values(?, ?)", seamId, "world".getBytes());

        List<byte[]> result = sqlite.queryListOfBytes("select invocation from invocations where seam_id = ?", seamId);

        assertThat(result).containsExactly(
            "hello".getBytes(),
            "world".getBytes()
        );
    }

}
