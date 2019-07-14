import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.junit.jupiter.api.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.lang.invoke.SerializedLambda;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosureSerializerTest {

    @Test
    public void testSerializableClosure() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(getClass()); // The closure's capturing class must be registered.
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());

        Callable<Integer> closure1 = (Callable<Integer> & java.io.Serializable) (() -> 72363);

        // The length cannot be checked reliable, as it can vary based on the JVM.
        //roundTrip(Integer.MIN_VALUE, closure1);

        Output output = new Output(1024, -1);
        kryo.writeObject(output, closure1);

        Input input = new Input(output.getBuffer(), 0, output.position());
        Callable<Integer> closure2 = (Callable<Integer>) kryo.readObject(input, ClosureSerializer.Closure.class);

        try {
            assertEquals(((Callable) closure1).call(), ((Callable) closure2).call());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}