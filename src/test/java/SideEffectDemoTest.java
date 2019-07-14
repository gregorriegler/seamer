import org.junit.jupiter.api.BeforeAll;
import seamer.SeamerFactory;
import seamer.core.Seam;
import seamer.test.SideEffectSeamTest;

public class SideEffectDemoTest extends SideEffectSeamTest {

    @BeforeAll
    @Override
    public void setup() {
        SideEffectDemo sideeffectDemo = new SideEffectDemo();

        for (int i = 0; i < 10; i++) {
            sideeffectDemo.entrypoint("hello ", i);
        }

        super.setup();
    }

    @Override
    public Object createCarrier() {
        return new SideEffectDemo();
    }

    public static class SideEffectDemo {

        public void entrypoint(String arg1, Integer arg2) {
            Seam<String> sideEffectSeam = a -> blackbox((String) a[0], (Integer) a[1]);
            String result = SeamerFactory.createAndPersist(sideEffectSeam, this)
                .executeAndRecord(arg1, arg2);
            System.out.println(result);
        }

        public String effect = "";

        public String blackbox(String arg1, Integer arg2) {
            effect += arg2;
            return arg1 + effect;
        }

    }
}
