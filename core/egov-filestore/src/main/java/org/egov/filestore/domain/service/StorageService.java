package org.egov.filestore.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.filestore.domain.exception.EmptyFileUploadRequestException;
import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.domain.model.FileInfo;
import org.egov.filestore.domain.model.FileLocation;
import org.egov.filestore.domain.model.Resource;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageService {

	private static final String UPLOAD_MESSAGE = "Received upload request for " +
			"jurisdiction: %s, module: %s, tag: %s with file count: %s";

	private ArtifactRepository artifactRepository;
    private IdGeneratorService idGeneratorService;

    @Autowired
    public StorageService(ArtifactRepository artifactRepository,
                          IdGeneratorService idGeneratorService) {
        this.artifactRepository = artifactRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public List<String> save(List<MultipartFile> filesToStore, String jurisdictionId, String module, String tag) {
		validateFilesToUpload(filesToStore, jurisdictionId, module, tag);
		log.info(UPLOAD_MESSAGE, jurisdictionId, module, tag, filesToStore.size());
		List<Artifact> artifacts =
                mapFilesToArtifacts(filesToStore, jurisdictionId, module, tag);
        return this.artifactRepository.save(artifacts);
    }

	private void validateFilesToUpload(List<MultipartFile> filesToStore, String jurisdictionId, String module, String
			tag) {
		if (CollectionUtils.isEmpty(filesToStore)) {
			throw new EmptyFileUploadRequestException(jurisdictionId, module, tag);
		}
	}

	private List<Artifact> mapFilesToArtifacts(List<MultipartFile> files,
                                               String jurisdictionId,
                                               String module,
                                               String tag) {
        return files.stream()
                .map(file -> {
                    FileLocation fileLocation =
                            new FileLocation(this.idGeneratorService.getId(), module, jurisdictionId, tag);
                    return new Artifact(file, fileLocation);
                })
                .collect(Collectors.toList());
    }

    public Resource retrieve(String fileStoreId) {
        return artifactRepository.find(fileStoreId);
    }

    public List<FileInfo> retrieveByTag(String tag) {
        return artifactRepository.findByTag(tag);
    }
}
