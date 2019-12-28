package seamer.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.SeamerFactory;
import seamer.core.Seamer;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ClosureSeamTest {

    private static Seamer seamer;

    @BeforeAll
    public void reset() {
        SeamerFactory.reset(seamId());
    }

    @BeforeEach
    public void setup() {
        seamer = SeamerFactory.load(carrierClass(), seamId());
    }

    public abstract Class carrierClass();

    public abstract String seamId();

    public Stream<Arguments> invocations() {
        return SeamerFactory.loadInvocations(seamId())
            .stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllinvocations(Object[] args, Object expected) {
        Object actual = seamer.execute(args);
        assertEquals(expected, actual);
    }
}
