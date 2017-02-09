package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.entity.FileStoreMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArtifactRepository {

    private DiskFileStoreRepository diskFileStoreRepository;
    private FileStoreMapperRepository fileStoreMapperRepository;

    public ArtifactRepository(DiskFileStoreRepository diskFileStoreRepository, FileStoreMapperRepository fileStoreMapperRepository) {
        this.diskFileStoreRepository = diskFileStoreRepository;
        this.fileStoreMapperRepository = fileStoreMapperRepository;
    }

    public List<FileStoreMapper> storeArtifacts(List<Artifact> artifacts, String tenantId, String module) throws IOException {
        this.diskFileStoreRepository.storeFilesOnDisk(artifacts, tenantId, module);
        return this.fileStoreMapperRepository.save(mapArtifactsListToEntitiesList(artifacts));
    }

    private List<FileStoreMapper> mapArtifactsListToEntitiesList(List<Artifact> artifacts) {
        return artifacts.stream().map(this::mapArtifactToFileStoreMapper).collect(Collectors.toList());
    }

    private FileStoreMapper mapArtifactToFileStoreMapper(Artifact artifact) {
        FileStoreMapper fileStoreMapper = new FileStoreMapper();
        fileStoreMapper.setFileStoreId(artifact.getFileStoreId());
        fileStoreMapper.setFileName(artifact.getMultipartFile().getOriginalFilename());
        fileStoreMapper.setContentType(artifact.getMultipartFile().getContentType());

        return fileStoreMapper;
    }
}
