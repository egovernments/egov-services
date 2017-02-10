package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.model.Artifact;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DiskFileStoreRepository {

    private FileRepository fileRepository;

    public DiskFileStoreRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void write(List<Artifact> artifacts) {
        artifacts.forEach(artifact -> {
            MultipartFile multipartFile = artifact.getMultipartFile();
            fileRepository.write(multipartFile, artifact.getPath());
        });
    }

}

