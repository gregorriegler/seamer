package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileBasedPersistenceShould {

    private final FileBasedPersistence persistence = new FileBasedPersistence();

    @Test
    void create_FileSeamRepository() {
        SeamRepository seams = persistence.createSeams(KryoFactory.createSerializer());

        assertThat(seams).isInstanceOf(FileSeamRepository.class);
    }
    @Test
    void create_FileInvocations() {
        Invocations invocations = persistence.createInvocations(KryoFactory.createSerializer());

        assertThat(invocations).isInstanceOf(FileInvocations.class);
    }
}