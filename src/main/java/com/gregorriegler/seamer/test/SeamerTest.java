package com.gregorriegler.seamer.test;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.Seam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SeamerTest {

    protected Seam<?> seam;

    @BeforeAll
    public void setup() {
        seam = Seamer.load(carrierClass(), seamId());
    }

    public abstract Class<?> carrierClass();

    public abstract String seamId();

    public Stream<Arguments> invocations() {
        return seam.getInvocations()
            .stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("invocations")
    public void testAllInvocations(Object[] args, Object expected) {
        Object actual = seam.execute(args);
        assertEquals(expected, actual);
    }
}
