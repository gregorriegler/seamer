package com.gregorriegler.seamer.sqlite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteShould {

    private final String seamName = "irrelevant";
    private final byte[] seam = "hello".getBytes();
    private Sqlite sqlite;

    @BeforeEach
    void setUp() {
        sqlite = new Sqlite("jdbc:sqlite::memory:");
        createSchema();
    }

    @AfterEach
    void tearDown() {
        sqlite.close();
    }

    @Test
    void persist_and_retrieve_data() {
        sqlite.parameterizedCommand("insert into seams values(?, ?)", seamName, seam);

        byte[] result = sqlite.queryBytes("select invokable from seams where id = ?", seamName).get();

        assertThat(result).isEqualTo(seam);
    }

    @Test
    void not_find_non_existent_data() {
        Optional<byte[]> result = sqlite.queryBytes("select invokable from seams where id = ?", seamName);

        assertThat(result).isEmpty();
    }

    private void createSchema() {
        sqlite.command(
            "drop table if exists seams",
            "create table seams (id string, invokable blob)"
        );
    }

}
