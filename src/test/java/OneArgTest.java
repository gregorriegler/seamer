import org.junit.jupiter.api.BeforeAll;
import seamer.core.Seamer;
import seamer.test.PureSeamTest;

public class OneArgTest extends PureSeamTest {

    @BeforeAll
    public static void setup() {
        OneArgDemo oneArgDemo = new OneArgDemo();

        for (int i = 0; i < 50; i++) {
            oneArgDemo.entrypoint(i);
        }
    }

    @Override
    public Object createCarrier() {
        return new OneArgDemo();
    }

    public static class OneArgDemo {

        public void entrypoint(Integer arg1) {
            Integer result = Seamer
                .createAndPersist(a -> blackbox((Integer) a[0]), new OneArgDemo())
                .executeAndRecord(arg1);

            System.out.println(result);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }

}
