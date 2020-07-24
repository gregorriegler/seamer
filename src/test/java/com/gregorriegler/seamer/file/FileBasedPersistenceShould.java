package com.gregorriegler.seamer.file;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileBasedPersistenceShould {

    private FileBasedPersistence<String> persistence = new FileBasedPersistence<>();

    @Test
    void create_FileSeamRepository() {
        SeamRepository<String> seams = persistence.createSeams();

        assertThat(seams).isInstanceOf(FileSeamRepository.class);
    }
    @Test
    void create_FileInvocations() {
        Invocations invocations = persistence.createInvocations();

        assertThat(invocations).isInstanceOf(FileInvocations.class);
    }
}