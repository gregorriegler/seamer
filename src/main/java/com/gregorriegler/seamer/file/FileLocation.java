package com.gregorriegler.seamer.file;

import java.io.File;

public interface FileLocation {
    String DEFAULT_BASE_PATH = "target/seamer";
    String INVOCATIONS_FILE = "invocations";
    String SEAM_FILE = "seam";

    static File seamFile(String basePath, String seamId) {
        return new File(seamDir(basePath, seamId) + File.separator + SEAM_FILE);
    }

    static File invocationsFile(String basePath, String seamId) {
        return new File(seamDir(basePath, seamId) + File.separator + INVOCATIONS_FILE);
    }

    static String seamDir(String basePath, String seamId) {
        return basePath + File.separator + seamId;
    }

    static void createSeamDir(String basePath, String seamId) {
        File seamDir = seamDirAsFile(basePath, seamId);
        if (!seamDir.exists()) seamDir.mkdirs();
        seamDir.mkdirs();
    }

    static File seamDirAsFile(String basePath, String seamId) {
        return new File(seamDir(basePath, seamId));
    }
}
