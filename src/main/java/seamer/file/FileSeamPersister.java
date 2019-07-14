package seamer.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Seam;
import seamer.core.SeamPersister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.invoke.SerializedLambda;

public class FileSeamPersister implements SeamPersister {

    private static final Logger LOG = LoggerFactory.getLogger(FileSeamPersister.class);
    private static final String DEFAULT_BASE_PATH = "target/seamer";
    public static final String SEAM_FILE = "seam";

    public FileSeamPersister() {
    }

    @Override
    public void persist(String seamId, Seam seam, Object carrier) {
        try {
            Kryo kryo = createKryo(carrier);
            createDir(seamId);
            File seamFile = seamFile(seamId);
            if (seamFile.exists()) return;
            LOG.info("persisting seam at {}", seamFile.getAbsolutePath());
            Output fileOutput = new Output(new FileOutputStream(seamFile));
            kryo.writeClassAndObject(fileOutput, seam);
            fileOutput.flush();
            fileOutput.close();
        } catch (FileNotFoundException e) {
            LOG.error("failed to persist seam", e);
        }
    }

    public static File seamFile(String seamId) {
        return new File(persistentFilePath(seamId) + File.separator + SEAM_FILE);
    }

    public void createDir(String seamId) {
        String seamPath = persistentFilePath(seamId);
        File seamDir = new File(seamPath);
        if(!seamDir.exists()) seamDir.mkdirs();
    }

    public static String persistentFilePath(String seamId) {
        return DEFAULT_BASE_PATH + File.separator + seamId;
    }

    public static Kryo createKryo(Object carrier) {
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