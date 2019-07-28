package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.SideEffectSeamTest;

public class SideEffectTest extends SideEffectSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(SideEffectTest.class);

    @BeforeAll
    @Override
    public void setup() {
        SideEffectDemo sideEffectDemo = new SideEffectDemo();

        for (int i = 0; i < 5; i++) {
            sideEffectDemo.entrypoint("hello ", i);
        }

        super.setup();
    }

    @Override
    public Object createCarrier() {
        return new SideEffectDemo();
    }

    public static class SideEffectDemo {

        public void entrypoint(String arg1, Integer arg2) {
            String result = SeamerFactory.createAndPersist(a -> blackbox((String) a[0], (Integer) a[1]), this)
                .executeAndRecord(arg1, arg2);

            LOG.info(result);
        }

        public String effect = "";

        public String blackbox(String arg1, Integer arg2) {
            effect += arg2;
            return arg1 + effect;
        }
    }
}
