package org.egov.pgr;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestResourceReader {
    public String readResource(String resource) throws IOException {
        File file = ResourceUtils.getFile(this.getClass().getResource("/getServiceRequests.txt"));
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }
}

