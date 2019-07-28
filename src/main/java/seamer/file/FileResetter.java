package seamer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.SeamResetter;

import java.io.File;

public class FileResetter implements SeamResetter {

    private static final Logger LOG = LoggerFactory.getLogger(FileResetter.class);

    @Override
    public void resetAll() {
        deleteFolder(new File(FileInvocationRecorder.DEFAULT_DIR));
        LOG.info("reset seamer's data");
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
