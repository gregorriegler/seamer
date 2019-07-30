package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.SideEffectSeamTest;

public class SideEffectIT extends SideEffectSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(SideEffectIT.class);
    private static final String SEAM_ID = SideEffectIT.class.getName();

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
    public Class carrierClass() {
        return SideEffectDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class SideEffectDemo {

        public void entrypoint(String arg1, Integer arg2) {
            String result = SeamerFactory.createAndPersist(
                a -> blackbox((String) a[0], (Integer) a[1]),
                this.getClass(),
                SEAM_ID
            ).executeAndRecord(arg1, arg2);

            LOG.info(result);
        }

        public String effect = "";

        public String blackbox(String arg1, Integer arg2) {
            effect += arg2;
            return arg1 + effect;
        }
    }
}
