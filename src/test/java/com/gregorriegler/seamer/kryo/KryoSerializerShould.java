package com.gregorriegler.seamer.kryo;

import com.gregorriegler.seamer.core.ProxySignature;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class KryoSerializerShould {

    private final KryoSerializer serializer = KryoFactory.createSerializer(this.getClass());

    @Test
    void serialize_and_deserialize() {
        ProxySignature<String> expected = ProxySignature.of("someObject", "toString");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        serializer.serialize(expected, outputStream);
        ProxySignature<?> result = serializer.deserialize(from(outputStream), ProxySignature.class);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void deserialize_a_list() {
        List<String> expected = asList("a", "b", "c");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        serializer.serialize(expected.get(0), outputStream);
        serializer.serialize(expected.get(1), outputStream);
        serializer.serialize(expected.get(2), outputStream);
        List<String> result = serializer.deserializeList(from(outputStream), String.class);

        assertThat(result).isEqualTo(expected);
    }

    private ByteArrayInputStream from(ByteArrayOutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
