import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.Call;
import seamer.Seamer;
import seamer.file.FileCallLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SideEffectSeamerDemoTest {

    private static SideEffectSeamerDemo carrier;
    private static Seamer<String> seamer;

    @BeforeAll
    public static void setup() {
        carrier = new SideEffectSeamerDemo();
        seamer = Seamer.load(carrier);
    }

    public static Stream<Arguments> calls() {
        FileCallLoader loader = new FileCallLoader();

        List<Call> calls = loader.load();
        return calls.stream()
            .map(c -> Arguments.of(c.getArgs()[0], c.getArgs()[1], c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("calls")
    void testAllCalls(String arg1, Integer arg2, String expected) {
        String actual = seamer.execute(arg1, arg2);
        assertEquals(expected, actual);
    }
}
