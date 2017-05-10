package org.egov.filestore.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
@Slf4j
public class FileRepository {

    public void write(MultipartFile file, Path path) {
        try {
            FileUtils.forceMkdirParent(path.toFile());
            file.transferTo(path.toFile());
            log.info("Successfully wrote file to disk. " +
                    "File name: " + file.getOriginalFilename() + " File Size: " + file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource read(Path path) {
        return new FileSystemResource(path.toFile());
    }

}
