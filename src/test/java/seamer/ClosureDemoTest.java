package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.SeamTest;

public class ClosureDemoTest extends SeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClosureDemoTest.class);
    private static final String SEAM_ID = ClosureDemoTest.class.getName();

    @BeforeAll
    @Override
    public void setup() {
        SeamerFactory.reset(seamId());

        ClosureDemo closureDemo = new ClosureDemo();

        for (int i = 0; i < 5; i++) {
            closureDemo.entrypoint("hello ", i);
        }

        super.setup();
    }

    @Override
    public Class<?> carrierClass() {
        return ClosureDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class ClosureDemo {

        public void entrypoint(String arg1, Integer arg2) {
            String result = SeamerFactory.intercept(
                a -> blackbox((String) a[0], (Integer) a[1]),
                this.getClass(),
                SEAM_ID
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
