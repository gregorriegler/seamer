package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.cglib.SeamerCglibFactory;
import seamer.core.Invocation;
import seamer.core.Seamer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CglibProxySeamIT {

    public static final String SEAM_ID = CglibProxySeamIT.class.getName();
    private Seamer seamer;

    @BeforeAll
    void setUp() {
        SeamerFactory.reset(SEAM_ID);

        ProxyDemo proxyDemo = SeamerCglibFactory.createProxySeam(ProxyDemo.class, "blackbox", SEAM_ID);
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.doNotProxyThis("don't seam me!");
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);

        seamer = SeamerFactory.load(SEAM_ID, ProxyDemo.class);
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Object actual = seamer.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        List<Invocation> invocations = SeamerFactory.loadInvocations(SEAM_ID);
        return invocations.stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    public static class ProxyDemo {

        public String doNotProxyThis(String arg1) {
            return arg1;
        }

        public String blackbox(String arg1, Integer arg2) {
            String result = arg1 + arg2;
            return result;
        }
    }


}
