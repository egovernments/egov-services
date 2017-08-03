package org.egov.user.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
	public String getFileContents(String path) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(path).getFile()).toPath()));
	}

}
