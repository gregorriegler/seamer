package com.gregorriegler.seamer.kryo;

import com.gregorriegler.seamer.core.ProxySignature;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SerializeProxyTest {

    @Test
    void should_serialize_and_deserialize_a_proxy_signature() {
        ProxySignature<String> expected = ProxySignature.of("someObject", "toString");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        KryoSerializer serializer = new KryoSerializer(KryoFactory.createProxyKryo(this.getClass()));
        serializer.serialize(expected, outputStream);
        ProxySignature result = serializer.deserializeObject(from(outputStream), ProxySignature.class);

        assertThat(result).isEqualTo(expected);
    }

    private ByteArrayInputStream from(ByteArrayOutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
