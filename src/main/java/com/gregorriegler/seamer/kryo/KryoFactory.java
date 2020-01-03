package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.lang.invoke.SerializedLambda;

public class KryoFactory {
    public static Kryo createKryo(Class<?> carrierClass) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(carrierClass);
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        return kryo;
    }

    public static Kryo createKryo() {
        return new Kryo();
    }
}
