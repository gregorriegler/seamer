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
    private final String seamId;

    public FileSeamPersister(String seamId) {
        this.seamId = seamId;
    }

    @Override
    public void persist(Seam seam, Class carrierClass) {
        try {
            Kryo kryo = createKryo(carrierClass);
            FileLocation.createSeamDir(seamId);
            File seamFile = FileLocation.seamFile(seamId);
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

    @Override
    public boolean isPersisted() {
        return FileLocation.seamFile(seamId).exists();
    }

    public static Kryo createKryo(Class carrierClass) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(carrierClass);
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        return kryo;
    }
}