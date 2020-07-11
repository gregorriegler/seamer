package com.gregorriegler.seamer.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeamShould {
    @Test
    void be_constructed() {
        Seam<Object> seam = new Seam<>("id", seamStub());
        assertThat(seam).isNotNull();
    }

    @Test
    void not_be_constructed_without_id() {
        assertThatThrownBy(() -> new Seam<>(null, seamStub())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_seam() {
        assertThatThrownBy(() -> new Seam<>("id", null)).isInstanceOf(IllegalArgumentException.class);
    }

    private ProxySuture<Object> seamStub() {
        return ProxySuture.of(this, "test");
    }
}