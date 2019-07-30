package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.PureSeamTest;

public class OneArgIT extends PureSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(OneArgIT.class);
    private static final String SEAM_ID = OneArgIT.class.getName();

    @BeforeAll
    public static void setup() {
        OneArgDemo oneArgDemo = new OneArgDemo();

        for (int i = 0; i < 5; i++) {
            oneArgDemo.entrypoint(i);
        }
    }

    @Override
    public Class carrierClass() {
        return OneArgDemo.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class OneArgDemo {

        public void entrypoint(Integer arg1) {
            Integer result = SeamerFactory.createAndPersist(
                a -> blackbox((Integer) a[0]),
                this.getClass(),
                SEAM_ID
            ).executeAndRecord(arg1);

            LOG.info(result + "");
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }

}
