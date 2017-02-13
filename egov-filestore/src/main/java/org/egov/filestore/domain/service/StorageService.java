package org.egov.filestore.domain.service;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.domain.model.FileLocation;
import org.egov.filestore.domain.model.Resource;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private ArtifactRepository artifactRepository;
    private IdGeneratorService idGeneratorService;

    @Autowired
    public StorageService(ArtifactRepository artifactRepository,
                          IdGeneratorService idGeneratorService) {
        this.artifactRepository = artifactRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public List<String> save(List<MultipartFile> filesToStore, String jurisdictionId, String module, String tag) {
        List<Artifact> artifacts =
                mapFilesToArtifacts(filesToStore, jurisdictionId, module, tag);
        return this.artifactRepository.save(artifacts);
    }

    private List<Artifact> mapFilesToArtifacts(List<MultipartFile> files,
                                               String jurisdictionId,
                                               String module,
                                               String tag) {
        return files.stream()
                .map(file -> {

                    FileLocation fileLocation = FileLocation.builder()
                            .fileStoreId(this.idGeneratorService.getId())
                            .jurisdictionId(jurisdictionId)
                            .module(module)
                            .tag(tag)
                            .build();

                    return Artifact.builder()
                            .multipartFile(file)
                            .fileLocation(fileLocation)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Resource retrieve(String fileStoreId) {
        return artifactRepository.find(fileStoreId);
    }

    public List<String> retrieveByTag(String tag) {
        return artifactRepository.findByTag(tag)
                .stream()
                .map(FileLocation::getFileStoreId)
                .collect(Collectors.toList());
    }
}
