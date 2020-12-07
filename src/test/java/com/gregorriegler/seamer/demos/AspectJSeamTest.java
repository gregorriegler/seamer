package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.annotation.Seam;
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
        Seamer.verify(SEAM_ID);
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