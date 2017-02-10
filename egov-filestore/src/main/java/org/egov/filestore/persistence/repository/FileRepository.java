package org.egov.filestore.persistence.repository;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class FileRepository {

    public void write(MultipartFile file, Path path) {
        try {
            FileUtils.forceMkdirParent(path.toFile());
            file.transferTo(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
