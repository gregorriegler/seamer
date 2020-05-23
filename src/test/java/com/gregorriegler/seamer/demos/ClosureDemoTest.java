package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClosureDemoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClosureDemoTest.class);
    private static final String SEAM_ID = ClosureDemoTest.class.getName();

    @BeforeAll
    public static void setup() {
        Seamer.reset(SEAM_ID);

        ClosureDemo closureDemo = new ClosureDemo();

        for (int i = 0; i < 5; i++) {
            closureDemo.entryPoint("hello ", i);
        }

    }

    public String seamId() {
        return SEAM_ID;
    }

    @Test
    void verify() {
        Seamer.verify(seamId());
    }

    public static class ClosureDemo {

        public void entryPoint(String arg1, Integer arg2) {
            String result = Seamer.intercept(
                SEAM_ID,
                a -> blackbox(
                    (String) a[0],
                    (Integer) a[1]
                )
            ).invokeAndRecord(arg1, arg2);

            LOG.info(result);
        }

        public String state = "state";

        public String blackbox(String arg1, Integer arg2) {
            state += arg2;
            return arg1 + state;
        }
    }
}
