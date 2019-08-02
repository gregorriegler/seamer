package seamer.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.SeamerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PureSeamTest {

    @BeforeAll
    void setUp() {
        SeamerFactory.reset(seamId());
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
    void testAllInvocations(Object[] args, Object expected) {
        Object actual = SeamerFactory.load(seamId(), carrierClass()).execute(args);
        assertEquals(expected, actual);
    }
}
