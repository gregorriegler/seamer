package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

public class KryoFactory {
    public static KryoSerializer createSerializer() {
        return new KryoSerializer(createKryo());
    }

    private static Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        return kryo;
    }
}
