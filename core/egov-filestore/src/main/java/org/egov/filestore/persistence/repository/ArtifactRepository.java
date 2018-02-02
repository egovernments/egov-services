package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.exception.ArtifactNotFoundException;
import org.egov.filestore.domain.model.FileInfo;
import org.egov.filestore.domain.model.FileLocation;
import org.egov.filestore.domain.model.Resource;
import org.egov.filestore.persistence.entity.Artifact;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtifactRepository {

	private DiskFileStoreRepository diskFileStoreRepository;
	private FileStoreJpaRepository fileStoreJpaRepository;

	public ArtifactRepository(DiskFileStoreRepository diskFileStoreRepository,
			FileStoreJpaRepository fileStoreJpaRepository) {
		this.diskFileStoreRepository = diskFileStoreRepository;
		this.fileStoreJpaRepository = fileStoreJpaRepository;
	}

	public List<String> save(List<org.egov.filestore.domain.model.Artifact> artifacts) {
		//diskFileStoreRepository.write(artifacts);
		List<Artifact> artifactEntities = mapArtifactsListToEntitiesList(artifacts);
		return fileStoreJpaRepository.save(artifactEntities).stream()
				.map(Artifact::getFileStoreId)
				.collect(Collectors.toList());
	}

	private List<Artifact> mapArtifactsListToEntitiesList(List<org.egov.filestore.domain.model.Artifact> artifacts) {
		return artifacts.stream()
				.map(this::mapToEntity)
				.collect(Collectors.toList());
	}

	private Artifact mapToEntity(org.egov.filestore.domain.model.Artifact artifact) {
		Artifact artifactEntity = new Artifact();
		artifactEntity.setFileStoreId(artifact.getFileLocation().getFileStoreId());
		artifactEntity.setFileName(artifact.getMultipartFile().getOriginalFilename());
		artifactEntity.setContentType(artifact.getMultipartFile().getContentType());
		artifactEntity.setModule(artifact.getFileLocation().getModule());
		artifactEntity.setTag(artifact.getFileLocation().getTag());
		artifactEntity.setTenantId(artifact.getFileLocation().getTenantId());
		return artifactEntity;
	}

	public Resource find(String fileStoreId, String tenantId) throws IOException {
		Artifact artifact = fileStoreJpaRepository.findByFileStoreIdAndTenantId(fileStoreId, tenantId);
		if (artifact == null)
			throw new ArtifactNotFoundException(fileStoreId);

		org.springframework.core.io.Resource resource = diskFileStoreRepository.read(artifact.getFileLocation());
		return new Resource(artifact.getContentType(), artifact.getFileName(), resource, artifact.getTenantId(),resource.getFile().length());
	}

	public List<FileInfo> findByTag(String tag, String tenantId) {
		return fileStoreJpaRepository.findByTagAndTenantId(tag, tenantId).stream().map(this::mapArtifactToFileInfo)
				.collect(Collectors.toList());
	}

	private FileInfo mapArtifactToFileInfo(Artifact artifact) {
		FileLocation fileLocation = new FileLocation(artifact.getFileStoreId(), artifact.getModule(),
				 artifact.getTag(),artifact.getTenantId());

		return new FileInfo(artifact.getContentType(), fileLocation, artifact.getTenantId());
	}
}
