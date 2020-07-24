package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Persistence;
import com.gregorriegler.seamer.core.SeamRepositoryShould;

public class FileSeamRepositoryShould extends SeamRepositoryShould {
    @Override
    protected Persistence createPersistence() {
        return new FileBasedPersistence();
    }
}
