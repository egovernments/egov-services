package org.egov.filestore.domain.service;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.entity.FileStoreMapper;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private ArtifactRepository artifactRepository;
    private IdGeneratorService idGeneratorService;

    @Autowired
    public FileStorageService(ArtifactRepository artifactRepository, IdGeneratorService idGeneratorService) {
        this.artifactRepository = artifactRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public Resource getFile() {
        return null;
    }

    public List<FileStoreMapper> storeFiles(List<MultipartFile> filesToStore, String tenantId, String module) throws IOException {
        return this.artifactRepository.storeArtifacts(mapMultipartFileListToArtifactList(filesToStore), tenantId, module);
    }

    private List<Artifact> mapMultipartFileListToArtifactList(List<MultipartFile> filesToStore) {
        return filesToStore.stream()
                .map(fileToStore -> new Artifact(fileToStore, this.idGeneratorService.getId()))
                .collect(Collectors.toList());
    }
}
