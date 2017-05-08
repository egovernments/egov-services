package org.egov.pgrrest;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestResourceReader {
    public String readResource(String resource) throws IOException {
        File file = ResourceUtils.getFile(this.getClass().getResource("/getServiceRequests.json"));
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }
}

