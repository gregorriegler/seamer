import seamer.test.PureSeamTest;

public class TwoArgDemoTest extends PureSeamTest<String> {

    @Override
    public Object createCarrier() {
        return new TwoArgDemo();
    }
}
