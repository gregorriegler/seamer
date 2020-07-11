package com.gregorriegler.seamer.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeamShould {
    @Test
    void be_constructed() {
        SeamWithId<Object> seam = new SeamWithId<>("id", seamStub());
        assertThat(seam).isNotNull();
    }

    @Test
    void not_be_constructed_without_id() {
        assertThatThrownBy(() -> new SeamWithId<>(null, seamStub())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_seam() {
        assertThatThrownBy(() -> new SeamWithId<>("id", null)).isInstanceOf(IllegalArgumentException.class);
    }

    private ProxySeam<Object> seamStub() {
        return ProxySeam.of(this, "test");
    }
}