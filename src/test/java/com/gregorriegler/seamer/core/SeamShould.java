package com.gregorriegler.seamer.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeamShould {
    @Test
    void be_constructed() {
        Seam<Object> seam = new Seam<>("id", invokableStub(), invocationsStub());
        assertThat(seam).isNotNull();
    }

    @Test
    void not_be_constructed_without_id() {
        assertThatThrownBy(() -> new Seam<>(null, invokableStub(), invocationsStub())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_invokable() {
        assertThatThrownBy(() -> new Seam<>("id", null, invocationsStub())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void not_be_constructed_without_invocations() {
        assertThatThrownBy(() -> new Seam<>("id", invokableStub(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    private ProxyInvokable<Object> invokableStub() {
        return ProxyInvokable.of(this, "test");
    }

    private Invocations invocationsStub() {
        return new Invocations() {
            @Override
            public void record(String seamId, Invocation invocation) {

            }

            @Override
            public List<Invocation> getAll(String seamId) {
                return null;
            }
        };
    }
}