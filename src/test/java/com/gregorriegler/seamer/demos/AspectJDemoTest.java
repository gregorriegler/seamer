package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.Seam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AspectJDemoTest {

    public static final String SEAM_ID = "AspectJ";

    @BeforeAll
    void setUp() {
        Seamer.reset(SEAM_ID);

        AspectJDemo aspectJDemo = new AspectJDemo();
        aspectJDemo.blackbox("hello", 1);
        aspectJDemo.blackbox("world", 2);
        aspectJDemo.doNotProxyThis("don't seam me!");
        aspectJDemo.blackbox("hello", 3);
        aspectJDemo.blackbox("world", 4);
    }

    @Test
    void verify() {
        Seamer.verify(SEAM_ID);
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Seam<?> seam = Seamer.load(SEAM_ID);

        Object actual = seam.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        return Seamer.invocationsAsArguments(SEAM_ID);
    }

    public static class AspectJDemo {

        public String doNotProxyThis(String arg1) {
            return arg1;
        }

        @com.gregorriegler.seamer.core.annotation.Seam(SEAM_ID)
        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }
    }
}