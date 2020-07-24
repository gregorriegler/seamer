package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SqlitePersistenceShould {

    private SqlitePersistence persistence = new SqlitePersistence();

    @Test
    void create_SqliteSeamRepository() {
        SeamRepository seams = persistence.createSeams();

        assertThat(seams).isInstanceOf(SqliteSeamRepository.class);
    }

    @Test
    void create_SqliteInvocations() {
        Invocations invocations = persistence.createInvocations();

        assertThat(invocations).isInstanceOf(SqliteInvocations.class);
    }
}