package org.egov.wcms.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    public String getFileContents(final String path) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        return new String(Files.readAllBytes(new File(classLoader.getResource(path).getFile()).toPath()));
    }
}
