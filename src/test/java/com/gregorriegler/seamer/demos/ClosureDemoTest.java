package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.test.SeamerTest;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClosureDemoTest extends SeamerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClosureDemoTest.class);
    private static final String SEAM_ID = ClosureDemoTest.class.getName();

    @BeforeAll
    @Override
    public void setup() {
        Seamer.reset(seamId());

        ClosureDemo closureDemo = new ClosureDemo();

        for (int i = 0; i < 5; i++) {
            closureDemo.entryPoint("hello ", i);
        }

        super.setup();
    }

    @Override
    public Class<?> capturingClass() {
        return ClosureDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class ClosureDemo {

        public void entryPoint(String arg1, Integer arg2) {
            String result = Seamer.intercept(
                SEAM_ID,
                this.getClass(),
                a -> blackbox(
                    (String) a[0],
                    (Integer) a[1]
                )
            ).invoke(arg1, arg2);

            LOG.info(result);
        }

        public String state = "state";

        public String blackbox(String arg1, Integer arg2) {
            state += arg2;
            return arg1 + state;
        }
    }
}
