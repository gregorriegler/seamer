package com.gregorriegler.seamer.sqlite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteShould {

    @Test
    void create_a_sqlite_db() {
        String expectedSeam = "expected seam name";
        String actualSeam = "";

        Sqlite sqlite = new Sqlite("jdbc:sqlite::memory:");
        sqlite.createSchema();
        sqlite.executeUpdate("insert into seams values('expected seam name')");
        actualSeam = sqlite.getSeam();
        sqlite.close();

        assertThat(actualSeam).isEqualTo(expectedSeam);
    }

}
