package com.gregorriegler.seamer;

import com.gregorriegler.seamer.sqlite.SqlitePersistence;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SeamerShould {

    @Test
    void be_created() {
        Seamer<Object> seamer = Seamer.create();

        assertThat(seamer).isNotNull();
    }

    @Test
    void be_created_with_custom_persistence() {
        Seamer<String> seamer = Seamer.<String>create(new SqlitePersistence<String>());

        assertThat(seamer).isNotNull();
    }
}
