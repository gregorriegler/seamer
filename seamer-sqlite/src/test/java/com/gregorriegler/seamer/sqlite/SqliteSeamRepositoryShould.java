package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.contracts.SeamRepositoryShould;
import com.gregorriegler.seamer.core.Serializer;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class SqliteSeamRepositoryShould extends SeamRepositoryShould {

    @TempDir
    static File tempDir;

    @Override
    protected SqliteSeamRepository createRepository(Serializer serializer) {
        return new SqliteSeamRepository("jdbc:sqlite:"+ tempDir.getAbsolutePath() + "/seams", serializer);
    }

}
