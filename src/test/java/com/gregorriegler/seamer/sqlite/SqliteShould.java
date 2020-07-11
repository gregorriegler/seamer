package com.gregorriegler.seamer.sqlite;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteShould {

    private final String seamName = "irrelevant";
    private final byte[] seam = "hello".getBytes();

    @Test
    void persist_and_retrieve_a_seam() {
        Sqlite sqlite = new Sqlite("jdbc:sqlite::memory:");

        sqlite.command(
            "drop table if exists seams",
            "create table seams (name string, object blob)"
        );

        sqlite.parameterizedCommand("insert into seams values(?, ?)", seamName, seam);

        byte[] result = sqlite.queryBytes("select object from seams where name = ?", seamName).get();

        assertThat(result).isEqualTo(seam);
        sqlite.close();
    }

    @Test
    void not_find_a_non_existent_seam() {
        Sqlite sqlite = new Sqlite("jdbc:sqlite::memory:");

        sqlite.command(
            "drop table if exists seams",
            "create table seams (name string, object blob)"
        );

        Optional<byte[]> result = sqlite.queryBytes("select object from seams where name = ?", seamName);

        assertThat(result).isEmpty();
        sqlite.close();
    }

}
