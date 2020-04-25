package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

public class KryoFactory {
    public static KryoSerializer createSerializer(Class<?> carrierClass) {
        return new KryoSerializer(createKryo(carrierClass));
    }

    private static Kryo createKryo(Class<?> clazz) {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        kryo.register(clazz);
        return kryo;
    }
}
