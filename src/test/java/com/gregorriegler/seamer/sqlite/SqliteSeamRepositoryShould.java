package com.gregorriegler.seamer.sqlite;

import com.gregorriegler.seamer.Stubs;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.Seam;
import com.gregorriegler.seamer.core.SeamRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteSeamRepositoryShould {

    @Test
    void start_empty() {
        SqliteSeamRepository repository = new SqliteSeamRepository();

        Optional<Seam> seam = repository.byId("seamId", Stubs.invocations());

        assertThat(seam).isEmpty();
    }

    private class SqliteSeamRepository implements SeamRepository {
        @Override
        public void persist(Seam seam) {

        }

        @Override
        public Optional<Seam> byId(String seamId, Invocations invocations) {
            return Optional.empty();
        }
    }
}
