import seamer.Seamer;

public class OneArgDemo {

    public static void main(String[] args) {
        OneArgDemo oneArgDemo = new OneArgDemo();

        for (int i = 0; i < 50; i++) {
            oneArgDemo.entrypoint(i);
        }
    }

    public void entrypoint(Integer arg1) {
        Integer result = Seamer
            .create(a -> blackbox((Integer) a[0]))
            .executeAndRecord(arg1);

        System.out.println(result);
    }

    public Integer blackbox(Integer i) {
        return i * 2;
    }

}
