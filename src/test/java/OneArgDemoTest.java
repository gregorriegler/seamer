import seamer.test.PureSeamTest;

public class OneArgDemoTest extends PureSeamTest<Integer> {

    @Override
    public Object createCarrier() {
        return new OneArgDemo();
    }
}
