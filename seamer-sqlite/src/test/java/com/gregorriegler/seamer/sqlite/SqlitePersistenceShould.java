package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlitePersistenceShould {

    @Test
    void create_SqliteSeamRepository() {
        SeamRepository seams = new SqlitePersistence().createSeams(serializer());

        Assertions.assertThat(seams).isInstanceOf(SqliteSeamRepository.class);
    }

    @Test
    void create_SqliteInvocations() {
        Invocations invocations = new SqlitePersistence().createInvocations(serializer());

        Assertions.assertThat(invocations).isInstanceOf(SqliteInvocations.class);
    }

    private KryoSerializer serializer() {
        return KryoFactory.createSerializer();
    }
}