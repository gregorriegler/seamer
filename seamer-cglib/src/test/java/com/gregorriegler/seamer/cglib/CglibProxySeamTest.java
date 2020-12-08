package com.gregorriegler.seamer.cglib;

import com.gregorriegler.seamer.Seamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CglibProxySeamTest {

    public static final String SEAM_ID = CglibProxySeamTest.class.getName();
    private final Seamer seamer = Seamer.create();

    @BeforeEach
    void recordInvocations() {
        seamer.reset(SEAM_ID);

        ProxyDemo proxyDemo = SeamerCglibFactory.createProxySeam(ProxyDemo.class, "blackbox", SEAM_ID);
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.doNotProxyThis("don't seam me!");
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);
    }

    @Test
    void verify() {
        seamer.verify(SEAM_ID);
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
