import seamer.Seamer;

import java.io.Serializable;
import java.util.function.Function;

public class PersistableSeamerDemo {

    public static void main(String[] args) {
        PersistableSeamerDemo persistableSeamerDemo = new PersistableSeamerDemo();

        for (int i = 0; i < 50; i++) {
            persistableSeamerDemo.entrypoint("hello ", i);
        }
    }

    public void entrypoint(String arg1, Integer arg2) {
        String result = Seamer.createAndPersist((Function<Object[], String> & Serializable)(a -> blackbox((String) a[0], (Integer) a[1])), this)
            .executeAndRecord(arg1, arg2);

        System.out.println(result);
    }

    public static String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }

}
