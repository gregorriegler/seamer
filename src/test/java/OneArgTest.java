import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.SeamerFactory;
import seamer.test.PureSeamTest;

public class OneArgTest extends PureSeamTest {

    private static final Logger LOG = LoggerFactory.getLogger(OneArgTest.class);

    @BeforeAll
    public static void setup() {
        OneArgDemo oneArgDemo = new OneArgDemo();

        for (int i = 0; i < 5; i++) {
            oneArgDemo.entrypoint(i);
        }
    }

    @Override
    public Object createCarrier() {
        return new OneArgDemo();
    }

    public static class OneArgDemo {

        public void entrypoint(Integer arg1) {
            Integer result = SeamerFactory
                .createAndPersist(a -> blackbox((Integer) a[0]), new OneArgDemo())
                .executeAndRecord(arg1);

            LOG.info(result + "");
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }

}
