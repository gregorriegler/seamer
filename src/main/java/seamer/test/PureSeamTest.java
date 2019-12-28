package seamer.test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.SeamerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PureSeamTest {

    public abstract Class<?> carrierClass();

    public abstract String seamId();

    public Stream<Arguments> invocations() {
        return SeamerFactory.load(carrierClass(), seamId()).getInvocations()
            .stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Object actual = SeamerFactory.load(carrierClass(), seamId()).execute(args);
        assertEquals(expected, actual);
    }
}
