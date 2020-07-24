package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Persistence;
import com.gregorriegler.seamer.core.SeamRepositoryShould;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class SqliteSeamRepositoryShould extends SeamRepositoryShould {

    @TempDir
    static File tempDir;

    @Override
    protected Persistence createPersistence() {
        return new SqlitePersistence(jdbcUri());
    }

    private String jdbcUri() {
        return "jdbc:sqlite:"+ tempDir.getAbsolutePath() + "/seams";
    }

}
