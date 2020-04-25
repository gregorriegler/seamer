package com.gregorriegler.seamer;

import com.gregorriegler.seamer.cglib.SeamerCglibFactory;
import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Seam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CglibProxyDemoTest {

    public static final String SEAM_ID = CglibProxyDemoTest.class.getName();
    private Seam seam;

    @BeforeAll
    void setUp() {
        Seamer.reset(SEAM_ID);

        ProxyDemo proxyDemo = SeamerCglibFactory.createProxySeam(ProxyDemo.class, "blackbox", SEAM_ID);
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.doNotProxyThis("don't seam me!");
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);

        seam = Seamer.loadProxy(SEAM_ID, ProxyDemo.class);
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Object actual = seam.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        List<Invocation> invocations = Seamer.loadProxy(SEAM_ID, ProxyDemo.class).getInvocations();
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
