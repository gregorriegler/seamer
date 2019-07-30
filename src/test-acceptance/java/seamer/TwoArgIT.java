package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.PureSeamTest;

public class TwoArgIT extends PureSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(TwoArgIT.class);
    private static final String SEAM_ID = TwoArgIT.class.getName();

    @BeforeAll
    public static void setup() {
        TwoArgDemo twoArgDemo = new TwoArgDemo();

        for (int i = 0; i < 5; i++) {
            twoArgDemo.entrypoint("hello ", i);
        }
    }

    @Override
    public Class carrierClass() {
        return TwoArgDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class TwoArgDemo {

        public void entrypoint(String arg1, Integer arg2) {
            String result = SeamerFactory.createAndPersist(
                a -> blackbox((String) a[0], (Integer) a[1]),
                this.getClass(),
                SEAM_ID
            ).executeAndRecord(arg1, arg2);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }

    }
}
