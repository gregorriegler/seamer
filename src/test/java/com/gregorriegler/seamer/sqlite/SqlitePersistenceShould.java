package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import com.gregorriegler.seamer.kryo.KryoFactory;
import com.gregorriegler.seamer.kryo.KryoSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SqlitePersistenceShould {

    @TempDir
    static File tempDir;
    private final String customJdbcUri = "jdbc:sqlite:" + tempDir.getAbsolutePath() + "/seams";

    @Test
    void create_SqliteSeamRepository() {
        SeamRepository seams = new SqlitePersistence().createSeams(serializer());

        assertThat(seams).isInstanceOf(SqliteSeamRepository.class);
    }

    @Test
    void create_SqliteInvocations() {
        Invocations invocations = new SqlitePersistence().createInvocations(serializer());

        assertThat(invocations).isInstanceOf(SqliteInvocations.class);
    }

    @Test
    void create_SqliteSeamRepository_with_custom_uri() {
        SeamRepository seams = new SqlitePersistence(customJdbcUri).createSeams(serializer());
        Seam<String> expectedSeam = Stubs.seam();
        seams.persist(expectedSeam);

        Optional<Seam<String>> seam = new SqliteSeamRepository(customJdbcUri, serializer()).byId(Stubs.SEAM_ID, Stubs.invocations());

        assertThat(seam).contains(expectedSeam);
    }

    private KryoSerializer serializer() {
        return KryoFactory.createSerializer();
    }
}