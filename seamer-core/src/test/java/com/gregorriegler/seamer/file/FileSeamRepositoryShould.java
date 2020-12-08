package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.core.SeamRepositoryShould;
import com.gregorriegler.seamer.core.Serializer;

public class FileSeamRepositoryShould extends SeamRepositoryShould {
    @Override
    protected SeamRepository createRepository(Serializer serializer) {
        return new FileSeamRepository(serializer);
    }
}
