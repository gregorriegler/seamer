package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.core.Invocation;
import seamer.core.Seamer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProxyExecutionIT {

    public static final String SEAM_ID = ProxyExecutionIT.class.getName();

    @BeforeAll
    void setUp() {
        SeamerFactory.reset(SEAM_ID);

        ProxyDemo proxyDemo = SeamerFactory.createProxySeam(ProxyDemo.class, SEAM_ID);
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Seamer seamer = SeamerFactory.load(SEAM_ID, ProxyDemo.class);

        Object actual = seamer.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        List<Invocation> invocations = SeamerFactory.loadInvocations(SEAM_ID);
        return invocations.stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    public static class ProxyDemo {

        public String blackbox(String arg1, Integer arg2) {
            String result = arg1 + arg2 + "r";
            return result;
        }
    }


}
