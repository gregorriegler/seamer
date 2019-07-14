import seamer.Seamer;

public class TwoArgDemo {

    public static void main(String[] args) {
        TwoArgDemo twoArgDemo = new TwoArgDemo();

        for (int i = 0; i < 50; i++) {
            twoArgDemo.entrypoint("hello ", i);
        }
    }

    public void entrypoint(String arg1, Integer arg2) {
        String result = Seamer.createAndPersist(a -> blackbox((String) a[0], (Integer) a[1]), this)
            .executeAndRecord(arg1, arg2);

        System.out.println(result);
    }

    public static String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }

}
