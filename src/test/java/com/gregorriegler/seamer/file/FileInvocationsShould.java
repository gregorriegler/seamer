package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.InvocationsShould;
import com.gregorriegler.seamer.core.Persistence;

public class FileInvocationsShould extends InvocationsShould {

    @Override
    protected Persistence createPersistence() {
        return new FileBasedPersistence();
    }

}
