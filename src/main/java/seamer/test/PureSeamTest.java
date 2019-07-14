package seamer.test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.SeamerFactory;
import seamer.core.Call;
import seamer.core.Seamer;
import seamer.file.FileCallLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PureSeamTest {

    public abstract Object createCarrier();

    public Stream<Arguments> calls() {
        FileCallLoader loader = new FileCallLoader(SeamerFactory.idOf(createCarrier()));

        List<Call> calls = loader.load();
        return calls.stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("calls")
    void testAllCalls(Object[] args, Object expected) {
        Seamer seamer = SeamerFactory.load(createCarrier());

        Object actual = seamer.execute(args);

        assertEquals(expected, actual);
    }
}
