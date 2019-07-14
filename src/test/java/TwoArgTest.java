import org.junit.jupiter.api.BeforeAll;
import seamer.Seamer;
import seamer.test.PureSeamTest;

public class TwoArgTest extends PureSeamTest<String> {

    @BeforeAll
    public static void setup() {
        TwoArgDemo twoArgDemo = new TwoArgDemo();

        for (int i = 0; i < 50; i++) {
            twoArgDemo.entrypoint("hello ", i);
        }
    }

    @Override
    public Object createCarrier() {
        return new TwoArgDemo();
    }

    public static class TwoArgDemo {

        public void entrypoint(String arg1, Integer arg2) {
            String result = Seamer.createAndPersist(a -> blackbox((String) a[0], (Integer) a[1]), this)
                .executeAndRecord(arg1, arg2);

            System.out.println(result);
        }

        public String blackbox(String arg1, Integer arg2) {
            return arg1 + arg2;
        }

    }
}
