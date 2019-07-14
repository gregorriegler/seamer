package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamPersister;
import seamer.core.Seamer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.invoke.SerializedLambda;

public class FileSeamPersister implements SeamPersister {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamPersister.class);
    private static final String DEFAULT_BASE_PATH = "target/seamer";

    public FileSeamPersister() {
    }

    @Override
    public void persist(Seam seam, Object carrier) {
        try {
            Kryo kryo = createKryo(carrier);
            createDir(carrier);
            File seamFile = Seamer.seamFile(carrier);
            if (seamFile.exists()) return;
            Output fileOutput = new Output(new FileOutputStream(seamFile));
            kryo.writeClassAndObject(fileOutput, seam);
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