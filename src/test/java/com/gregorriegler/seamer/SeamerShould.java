package com.gregorriegler.seamer;

import com.gregorriegler.seamer.sqlite.SqlitePersistence;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SeamerShould {

    @Test
    void be_created_with_default_base_path() {
        Seamer seamer = Seamer.create();

        assertThat(seamer).isNotNull();
    }

    @Test
    void be_created_with_custom_base_path() {
        Seamer seamer = Seamer.create("/tmp/seamer");

        assertThat(seamer).isNotNull();
    }

    @Test
    void be_created_with_custom_persistence() {
        Seamer seamer = Seamer.create(new SqlitePersistence());

        assertThat(seamer).isNotNull();
    }
}
