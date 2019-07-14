package seamer.core;

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

public class Seamer<T> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Seamer.class);
    private static final String DEFAULT_BASE_PATH = "target/seamer";
    private static final String SEAM_FILE = "seam";

    public Seam<T> seam;
    private CallRecorder recorder;

    public static <T> Seamer<T> create(Seam<T> seam, Object carrier) {
        return new Seamer<>(seam, carrier);
    }

    public static <T> Seamer<T> createAndPersist(Seam<T> seam, Object carrier) {
        Seamer<T> seamer = create(seam, carrier);
        seamer.persist(carrier);
        return seamer;
    }

    public static <T> Seamer<T> load(Object carrier) {
        try {
            Kryo kryo = createKryo(carrier);
            Input fileInput = new Input(new FileInputStream(seamFile(carrier)));
            Object o = kryo.readClassAndObject(fileInput);
            return create((Seam)o, carrier);
        } catch (FileNotFoundException e) {
            LOG.error("failed to load seam", e);
            return null;
        }
    }

    public static File seamFile(Object carrier) {
        return new File(persistentFilePath(carrier) + File.separator + SEAM_FILE);
    }

    private Seamer(Seam<T> seam, Object carrier) {
        this.seam = seam;
        this.recorder = new FileCallRecorder(idOf(carrier));
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
            createDir(carrier);
            File seam = seamFile(carrier);
            if(seam.exists()) return;
            Output fileOutput = new Output(new FileOutputStream(seam));
            kryo.writeClassAndObject(fileOutput, this.seam);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    public void createDir(Object carrier) {
        String seamPath = persistentFilePath(carrier);
        File seamDir = new File(seamPath);
        if(!seamDir.exists()) seamDir.mkdirs();
    }

    public static String persistentFilePath(Object carrier) {
        return DEFAULT_BASE_PATH + File.separator + idOf(carrier);
    }

    public static String idOf(Object carrier) {
        return carrier.getClass().getName();
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
