package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.sqlite.SeamRepositoryShould;

public class FileSeamRepositoryShould extends SeamRepositoryShould {
    @Override
    protected SeamRepository createRepository() {
        return new FileSeamRepository(KryoFactory.createSerializer());
    }
}
