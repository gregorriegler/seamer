package com.gregorriegler.seamer.file;

import java.io.File;

public interface FileLocation {
    static String basePath() {
        return DEFAULT_BASE_PATH;
    }

    String DEFAULT_BASE_PATH = "src/test/seamer";
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
        if (!seamDir.exists() && !seamDir.mkdirs()) throw new RuntimeException("failed to create seamDir");
    }

    static File seamDirAsFile(String basePath, String seamId) {
        return new File(seamDir(basePath, seamId));
    }
}
