package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Signature2;
import com.gregorriegler.seamer.test.SeamerTest;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoArgSeamTest extends SeamerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TwoArgSeamTest.class);
    private static final String SEAM_ID = TwoArgSeamTest.class.getName();

    @BeforeAll
    @Override
    public void setup() {
        Seamer.reset(SEAM_ID);

        TwoArgDemo twoArgDemo = new TwoArgDemo();

        for (int i = 0; i < 5; i++) {
            twoArgDemo.entryPoint("hello ", i);
        }

        super.setup();
    }

    @Override
    public Class<?> carrierClass() {
        return TwoArgDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class TwoArgDemo {

        public void entryPoint(String string, Integer integer) {
            String result = Seamer.intercept(
                (Signature2<String, Integer, String>) (arg1, arg2) -> blackbox(arg1, arg2),
                this.getClass(),
                SEAM_ID
            ).invoke(string, integer);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }

    }
}
