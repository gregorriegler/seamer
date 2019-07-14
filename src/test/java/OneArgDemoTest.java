import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.Call;
import seamer.file.FileCallLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OneArgDemoTest {
    public static Stream<Arguments> calls() {
        FileCallLoader loader = new FileCallLoader();

        List<Call> calls = loader.load();
        return calls.stream()
            .map(c -> Arguments.of(c.getArgs()[0], c.getResult()));
    }

    @ParameterizedTest
    @MethodSource("calls")
    void testAllCalls(Integer input, Integer output) {
        OneArgDemo oneArgDemo = new OneArgDemo();

        Integer actual = oneArgDemo.blackbox(input);

        assertEquals(output, actual);
    }
}
