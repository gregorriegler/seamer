import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LambdaSerializationTest {

    @Test
    public void testLambdaInKryo() throws Exception {
        // Casting to Serializable forces java run-time to generate dedicated
        // methods supporting serialization of lambdas, e.g. writeReplace.
        Runnable r = (Runnable & Serializable) (() -> System.out.println("works"));
        Class c = r.getClass();
        Method writeReplace = c.getDeclaredMethod("writeReplace");
        Method readResolve = SerializedLambda.class.getDeclaredMethod("readResolve");
        writeReplace.setAccessible(true);
        readResolve.setAccessible(true);
        // Generate a serializable representation of this lambda
        Object replacement = writeReplace.invoke(r);
        if (replacement instanceof SerializedLambda) {
            SerializedLambda l = (SerializedLambda) replacement;
            // Serialize and deserialize the representation of this lambda
            // Use readResolve to create a real lambda object from this representation
            ((Runnable) readResolve.invoke(kryoSerDeser(l))).run();
        } else
            Assertions.fail("Could not serialize lambda");
    }

    @Test
    public void kryoLambda() {
        Consumer<String> consumer = (x) -> System.out.println("works" + x);

        Kryo kryo = new Kryo();
        byte[] bytes = new byte[500];
        kryo.writeClassAndObject(new Output(bytes), consumer);

        Consumer<String> loaded = (Consumer<String>) kryo.readClassAndObject(new Input(bytes));
        loaded.accept("works");
    }


    private <T> T kryoSerDeser (T r) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Output output = new Output(baos)) {
            kryo.writeClassAndObject(output, r);
        }

        try (Input input = new Input((new ByteArrayInputStream(baos.toByteArray())))) {
            return (T)kryo.readClassAndObject(input);
        }
    }

    @Test
    public void testLambdaWithoutArgsInKryo () {
        Kryo kryo = new Kryo();
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        kryo.register(LambdaSerializationTest.class);

        Callable<Integer> closure1 = (Callable<Integer> & java.io.Serializable)( () -> 72363 );

        Output output = new Output(1024, -1);
        kryo.writeObject(output, closure1);

        Input input = new Input(output.getBuffer(), 0, output.position());
        Callable<Integer> closure2 = (Callable<Integer>)kryo.readObject(input, ClosureSerializer.Closure.class);
        try {
            Integer result = closure2.call();
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
