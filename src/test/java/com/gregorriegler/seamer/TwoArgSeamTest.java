package com.gregorriegler.seamer;

import com.esotericsoftware.minlog.Log;
import com.gregorriegler.seamer.core.Signature2;
import com.gregorriegler.seamer.test.SeamerTest;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoArgSeamTest extends SeamerTest {

    private static final String SEAM_ID = "TwoArgSeamTest";

    @BeforeAll
    @Override
    public void setup() {
        Log.TRACE();
        Seamer.reset(SEAM_ID);

        SomeClass twoArgDemo = new SomeClass();

        for (int i = 0; i < 5; i++) {
            twoArgDemo.entryPoint("hello ", i);
        }

        seam = Seamer.load(SomeClass.class, seamId());
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
                (Signature2<String, Integer, String>) this::blackbox,
                this.getClass(),
                "TwoArgSeamTest"
            ).invoke(string, integer);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }

    }
}
