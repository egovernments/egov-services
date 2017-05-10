package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.domain.model.FileLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DiskFileStoreRepository {

    private FileRepository fileRepository;
    private String fileMountPath;

    public DiskFileStoreRepository(FileRepository fileRepository,
                                   @Value("${file.storage.mount.path}") String fileMountPath) {
        this.fileRepository = fileRepository;
        this.fileMountPath = fileMountPath;
    }

    public void write(List<Artifact> artifacts) {
        artifacts.forEach(artifact -> {
            MultipartFile multipartFile = artifact.getMultipartFile();
            FileLocation fileLocation = artifact.getFileLocation();
			Path path = getPath(fileLocation);
			fileRepository.write(multipartFile, path);
        });
    }

	public Resource read(FileLocation fileLocation) {
        Path path = getPath(fileLocation);
        return fileRepository.read(path);
    }

	private Path getPath(FileLocation fileLocation) {
		return Paths.get(fileMountPath, fileLocation.getTenantId(),
				fileLocation.getModule(),
				fileLocation.getFileStoreId());
	}


}

