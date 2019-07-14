import seamer.Seamer;

public class TwoArgsDemo {

    public static void main(String[] args) {
        TwoArgsDemo twoArgsDemo = new TwoArgsDemo();

        for (int i = 0; i < 50; i++) {
            twoArgsDemo.entrypoint("hello ", i);
        }
    }

    public void entrypoint(String arg1, Integer arg2) {
        String result = Seamer
            .create(a -> blackbox((String) a[0], (Integer) a[1]))
            .executeAndRecord(arg1, arg2);

        System.out.println(result);
    }

    public String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }

}
