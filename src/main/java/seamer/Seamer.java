package seamer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.file.FileCallRecorder;

import java.io.*;
import java.lang.invoke.SerializedLambda;
import java.util.function.Function;

public class Seamer<T> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Seamer.class);
    private static final String DEFAULT_PERSISTENT_FILE = "seamer/seam";

    public Function<Object[], T> seam;
    private CallRecorder recorder = new FileCallRecorder();

    public static <T> Seamer<T> create(Function<Object[], T> seam) {
        return new Seamer<>(seam);
    }

    public static <T> Seamer<T> createAndPersist(Function<Object[], T> seam, Object carrier) {
        Seamer<T> seamer = create(seam);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> load(Object carrier) {
        try {
            Kryo kryo = createKryo(carrier);
            Input fileInput = new Input(new FileInputStream(new File(DEFAULT_PERSISTENT_FILE)));
            Object o = kryo.readClassAndObject(fileInput);
            return create((Function)o);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return null;
        }
    }

    private Seamer(Function<Object[], T> seam) {
        this.seam = seam;
    }

    public T execute(Object... args) {
        T result = seam.apply(args);
        return result;
    }

    public T executeAndRecord(Object... args) {
        T result = execute(args);
        recorder.record(Call.of(args, result));
        return result;
    }

    public void persist(Object carrier) {
        try {
            Kryo kryo = createKryo(carrier);
            File seam = new File(DEFAULT_PERSISTENT_FILE);
            if(seam.exists()) return;
            Output fileOutput = new Output(new FileOutputStream(seam));
            kryo.writeClassAndObject(fileOutput, this.seam);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    private static Kryo createKryo(Object carrier) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(carrier.getClass());
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        return kryo;
    }
}
