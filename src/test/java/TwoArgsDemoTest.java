import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.Call;
import seamer.file.FileCallLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoArgsDemoTest {
    public static Stream<Arguments> calls() {
        FileCallLoader loader = new FileCallLoader();

        List<Call> calls = loader.load();
        return calls.stream()
            .map(c -> Arguments.of(c.getArgs()[0], c.getArgs()[1], c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("calls")
    void testAllCalls(String arg1, Integer arg2, String output) {
        TwoArgsDemo twoArgsDemo = new TwoArgsDemo();

        String actual = twoArgsDemo.blackbox(arg1, arg2);

        assertEquals(output, actual);
    }
}
