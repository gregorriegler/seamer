package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.InvokableWith2Arguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoArgumentSeamTest {

    private static final String SEAM_ID = "TwoArgSeamTest";
    private final Seamer seamer = Seamer.create();

    @BeforeEach
    public void recordInvocations() {
        seamer.reset(SEAM_ID);

        SomeClass someClass = new SomeClass();

        for (int i = 0; i < 5; i++) {
            someClass.entryPoint("hello ", i);
        }
    }

    @Test
    void verify() {
        seamer.get(SEAM_ID)
            .verify();
    }

    @Test
    void verifyComparingFields() {
        seamer.get(SEAM_ID)
            .verifyComparingFields();
    }

    @Test
    void verifyComparingToString() {
        seamer.get(SEAM_ID)
            .verifyComparingToString();
    }

    public static class SomeClass {

        private static final Logger LOG = LoggerFactory.getLogger(SomeClass.class);

        public void entryPoint(String string, Integer integer) {
            String result = Seamer.create()
                .define("TwoArgSeamTest", (InvokableWith2Arguments<String, Integer, String>) this::blackbox)
                .invokeAndRecord(string, integer);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }
    }
}
