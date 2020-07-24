package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.InvocationsShould;

public class SqliteInvocationsShould extends InvocationsShould {

    @Override
    protected SqlitePersistence createPersistence() {
        return new SqlitePersistence();
    }

}
