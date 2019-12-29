package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.test.SeamTest;

public class OneArgSeamTest extends SeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(OneArgSeamTest.class);
    private static final String SEAM_ID = OneArgSeamTest.class.getName();

    @BeforeAll
    @Override
    public void setup() {
        SeamerFactory.reset(SEAM_ID);

        OneArgDemo oneArgDemo = new OneArgDemo();

        for (int i = 0; i < 5; i++) {
            oneArgDemo.entrypoint(i);
        }

        super.setup();
    }

    @Override
    public Class<?> carrierClass() {
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
            ).interceptInvocation(arg1);

            LOG.info(result + "");
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }

}
