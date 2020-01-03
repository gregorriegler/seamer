package com.gregorriegler.seamer.file;

import java.io.File;

public interface FileLocation {
    String DEFAULT_DIR = "target/seamer";
    String INVOCATIONS_FILE = "invocations";
    String SEAM_FILE = "seam";

    static File seamFile(String seamId) {
        return new File(seamDir(seamId) + File.separator + SEAM_FILE);
    }

    static File invocationsFile(String seamId) {
        return new File(seamDir(seamId) + File.separator + INVOCATIONS_FILE);
    }

    static void createSeamDir(String seamId) {
        File seamDir = seamDirAsFile(seamId);
        if (!seamDir.exists()) seamDir.mkdirs();
        seamDir.mkdirs();
    }

    static String seamDir(String seamId) {
        return DEFAULT_DIR + File.separator + seamId;
    }

    static File seamDirAsFile(String seamId) {
        return new File(seamDir(seamId));
    }
}
