package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.SeamRepositoryShould;
import com.gregorriegler.seamer.core.Serializer;

public class SqliteSeamRepositoryShould extends SeamRepositoryShould {

    @Override
    protected SqliteSeamRepository createRepository(Serializer serializer) {
        return new SqliteSeamRepository("jdbc:sqlite:/tmp/seamertest", serializer);
    }

}
