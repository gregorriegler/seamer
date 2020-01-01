package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Signature1;
import seamer.test.SeamerTest;

public class OneArgSeamTest extends SeamerTest {

    private static final Logger LOG = LoggerFactory.getLogger(OneArgSeamTest.class);
    private static final String SEAM_ID = OneArgSeamTest.class.getName();

    @BeforeAll
    @Override
    public void setup() {
        Seamer.reset(SEAM_ID);

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
            Integer result = Seamer.intercept(
                (Signature1<Integer, Integer>) a -> blackbox(a),
                this.getClass(),
                SEAM_ID
            ).invoke(arg1);

            LOG.info(result + "");
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }

}
