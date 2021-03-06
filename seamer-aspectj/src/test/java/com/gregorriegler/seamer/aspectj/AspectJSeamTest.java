package com.gregorriegler.seamer.aspectj;

import com.gregorriegler.seamer.Seamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AspectJSeamTest {

    public static final String SEAM_ID = "AspectJ";
    private final Seamer seamer = Seamer.create();

    @BeforeEach
    void recordInvocations() {
        seamer.reset(SEAM_ID);

        AspectJDemo aspectJDemo = new AspectJDemo();
        aspectJDemo.blackbox("hello", 1);
        aspectJDemo.blackbox("world", 2);
        aspectJDemo.doNotProxyThis("don't seam me!");
        aspectJDemo.blackbox("hello", 3);
        aspectJDemo.blackbox("world", 4);
    }

    @Test
    void verify() {
        seamer.get(SEAM_ID)
            .verify();
    }

    public static class AspectJDemo {

        public String doNotProxyThis(String arg1) {
            return arg1;
        }

        @Seam(SEAM_ID)
        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }
    }
}