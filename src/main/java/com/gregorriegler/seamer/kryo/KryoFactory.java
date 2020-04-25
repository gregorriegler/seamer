package com.gregorriegler.seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import com.gregorriegler.seamer.core.ProxySignature;

import java.lang.invoke.SerializedLambda;

public class KryoFactory {
    public static Kryo createKryo(Class<?> clazz) {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        kryo.register(clazz);
        return kryo;
    }

    public static Kryo createProxyKryo(Class<?> clazz) {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(ProxySignature.class);
        kryo.register(clazz);
        return kryo;
    }

}
