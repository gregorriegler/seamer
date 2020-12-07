package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * seam interacts with and changes state in the capturing class
 */
public class ClosureSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClosureSeamTest.class);
    private static final String SEAM_ID = ClosureSeamTest.class.getName();
    private static final Seamer seamer = Seamer.create();

    @BeforeAll
    public static void recordInvocations() {
        seamer.reset(SEAM_ID);

        ClosureDemo closureDemo = new ClosureDemo();
        for (int i = 0; i < 5; i++) {
            closureDemo.entryPoint("hello ", i);
        }
    }

    @Test
    void verify() {
        seamer.verify(SEAM_ID);
    }

    public static class ClosureDemo {

        public void entryPoint(String arg1, Integer arg2) {
            String result = Seamer.create()
                .define(SEAM_ID, a -> blackbox((String) a[0], (Integer) a[1]))
                .invokeAndRecord(arg1, arg2);

            LOG.info(result);
        }

        public String state = "state";

        public String blackbox(String arg1, Integer arg2) {
            state += arg2;
            return arg1 + state;
        }
    }
}
