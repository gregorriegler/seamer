package com.gregorriegler.seamer.core;

import com.gregorriegler.seamer.contracts.Stubs;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeamShould {

    private final Invokable<String> invokableStub = Stubs.invokable();
    private final Invocations invocationsStub = Stubs.invocations();

    @Test
    void be_constructed() {
        Seam<String> seam = new Seam<>("id", invokableStub, invocationsStub);
        assertThat(seam).isNotNull();
    }

    @Test
    void not_be_constructed_without_id() {
        assertThatThrownBy(() -> new Seam<>(null, invokableStub, invocationsStub)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_invokable() {
        assertThatThrownBy(() -> new Seam<>("id", null, invocationsStub)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_invocations() {
        assertThatThrownBy(() -> new Seam<>("id", invokableStub, null)).isInstanceOf(IllegalArgumentException.class);
    }

}