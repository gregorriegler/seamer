import seamer.test.SideEffectSeamTest;

public class SideEffectDemoTest extends SideEffectSeamTest {

    @Override
    public Object createCarrier() {
        return new SideEffectDemo();
    }
}
