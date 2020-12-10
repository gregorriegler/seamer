package com.gregorriegler.seamer.core;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.Objects;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class Seam<T> {

    private final String id;
    private final Invokable<T> invokable;
    private final Invocations invocations;

    public Seam(String id, Invokable<T> invokable, Invocations invocations) {
        assertNotNull(id, "id is mandatory");
        assertNotNull(invokable, "seam is mandatory");
        assertNotNull(invocations, "invocations are mandatory");
        this.id = id;
        this.invokable = invokable;
        this.invocations = invocations;
    }

    public String id() {
        return id;
    }

    public Invokable<T> invokable() {
        return invokable;
    }

    public Invocations invocations() {
        return invocations;
    }

    public T invokeAndRecord(Object... args) {
        T result = invoke(args);
        record(args, result);
        return result;
    }

    public T invoke(Object... args) {
        return invokable.invoke(args);
    }

    public void record(Object[] args, T result) {
        invocations.record(id, Invocation.of(args, result));
    }

    public void verify() {
        verify(AbstractAssert::isEqualTo);
    }

    public void verifyComparingFields() {
        verify(AbstractObjectAssert::isEqualToComparingFieldByFieldRecursively);
    }

    public void verifyComparingToString() {
        verify((anAssert, expected) -> anAssert.asString().isEqualTo(expected.toString()));
    }

    public void verify(BiConsumer<ObjectAssert<T>, Object> verification) {
        for (Invocation invocation : invocations.getAll(id)) {
            T actual = invoke(invocation.getArgs());
            ObjectAssert<T> objectAssert = assertThat(actual);
            verification.accept(objectAssert, invocation.getResult());
        }
    }

    private void assertNotNull(Object var, String message) {
        if (var == null) throw new IllegalArgumentException(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seam<?> seam = (Seam<?>) o;
        return Objects.equals(id, seam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Seam{" + id + '}';
    }
}
