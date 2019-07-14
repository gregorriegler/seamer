import seamer.Seam;
import seamer.Seamer;

public class SideEffectDemo {

    public static void main(String[] args) {
        SideEffectDemo sideeffectDemo = new SideEffectDemo();

        for (int i = 0; i < 50; i++) {
            sideeffectDemo.entrypoint("hello ", i);
        }
    }

    public void entrypoint(String arg1, Integer arg2) {
        Seam<String> sideEffectSeam = a -> blackbox((String) a[0], (Integer) a[1]);
        String result = Seamer.createAndPersist(sideEffectSeam, this)
            .executeAndRecord(arg1, arg2);
        System.out.println(result);
    }

    public String effect = "";

    public String blackbox(String arg1, Integer arg2) {
        effect += arg2;
        return arg1 + effect;
    }

}
