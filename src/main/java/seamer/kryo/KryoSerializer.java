package seamer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import seamer.core.Invocation;
import seamer.core.Seam;
import seamer.file.Serializer;

import java.io.InputStream;
import java.io.OutputStream;

public class KryoSerializer implements Serializer {
    private final Kryo kryo;

    public KryoSerializer(Class<?> carrierClass) {
        this.kryo = KryoFactory.createKryo(carrierClass);
    }

    public KryoSerializer() {
        this.kryo = KryoFactory.createKryo();
    }

    @Override
    public void serialize(Seam<?> seam, OutputStream outputStream) {
        Output fileOutput = new Output(outputStream);
        kryo.writeClassAndObject(fileOutput, seam);
        fileOutput.close();
    }

    @Override
    public void serialize(Invocation invocation, OutputStream outputStream) {
        Output fileOutput = new Output(outputStream);
        kryo.writeClassAndObject(fileOutput, invocation);
        fileOutput.close();
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        Input fileInput = new Input(inputStream);
        return kryo.readClassAndObject(fileInput);
    }
}