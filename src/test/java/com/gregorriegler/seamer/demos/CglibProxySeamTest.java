package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.cglib.SeamerCglibFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CglibProxySeamTest {

    public static final String SEAM_ID = CglibProxySeamTest.class.getName();

    @BeforeAll
    void recordInvocations() {
        Seamer.reset(SEAM_ID);

        ProxyDemo proxyDemo = SeamerCglibFactory.createProxySeam(ProxyDemo.class, "blackbox", SEAM_ID);
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.doNotProxyThis("don't seam me!");
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);
    }

    @Test
    void verify() {
        Seamer.verify(SEAM_ID);
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
