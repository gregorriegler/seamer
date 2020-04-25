package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.SignatureWith2Arguments;
import com.gregorriegler.seamer.test.SeamerTest;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoArgSeamTest extends SeamerTest {

    private static final String SEAM_ID = "TwoArgSeamTest";

    @BeforeAll
    @Override
    public void setup() {
        Seamer.reset(SEAM_ID);

        SomeClass someClass = new SomeClass();

        for (int i = 0; i < 5; i++) {
            someClass.entryPoint("hello ", i);
        }

        seam = Seamer.load(seamId(), SomeClass.class);
    }

    @Override
    public Class<?> capturingClass() {
        return SomeClass.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class SomeClass {
        private static final Logger LOG = LoggerFactory.getLogger(SomeClass.class);

        public void entryPoint(String string, Integer integer) {
            String result = Seamer.intercept(
                "TwoArgSeamTest",
                this.getClass(),
                (SignatureWith2Arguments<String, Integer, String>) this::blackbox
            ).invoke(string, integer);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }

    }
}
