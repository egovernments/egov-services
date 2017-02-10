package org.egov.filestore.domain.service;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private ArtifactRepository artifactRepository;
    private IdGeneratorService idGeneratorService;
    private String fileStorageMountPath;

    @Autowired
    public StorageService(ArtifactRepository artifactRepository,
                          IdGeneratorService idGeneratorService,
                          @Value("${fileStorageMountPath}") String fileStorageMountPath) {
        this.artifactRepository = artifactRepository;
        this.idGeneratorService = idGeneratorService;
        this.fileStorageMountPath = fileStorageMountPath;
    }

    public List<String> save(List<MultipartFile> filesToStore, String jurisdictionId, String module)  {
        List<Artifact> artifacts =
                mapFilesToArtifacts(filesToStore, jurisdictionId, module);
        return this.artifactRepository.save(artifacts);
    }

    private List<Artifact> mapFilesToArtifacts(List<MultipartFile> files,
                                               String jurisdictionId,
                                               String module) {
        return files.stream()
                .map(file -> Artifact.builder()
                        .multipartFile(file)
                        .fileStoreId(this.idGeneratorService.getId())
                        .module(module)
                        .jurisdictionId(jurisdictionId)
                        .fileStorageMountPath(fileStorageMountPath)
                        .build())
                .collect(Collectors.toList());
    }
}
