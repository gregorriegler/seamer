package com.gregorriegler.seamer.sqlite;

public class SqliteSeamRepositoryShould extends SeamRepositoryShould {

    @Override
    protected SqliteSeamRepository createRepository() {
        return new SqliteSeamRepository("jdbc:sqlite:/tmp/seamertest");
    }

}
