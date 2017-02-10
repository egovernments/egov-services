package org.egov.filestore.persistence.repository;

import org.egov.filestore.persistence.entity.Artifact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactRepositoryTest {

    @Mock
    DiskFileStoreRepository diskFileStoreRepository;

    @Mock
    FileStoreJpaRepository fileStoreJpaRepository;

    @Captor
    private ArgumentCaptor<List<Artifact>> listArgumentCaptor;

    private final String TENANT_ID = "mumbai";
    private final String MODULE = "pgr";
    private final String FILE_STORAGE_MOUNT_PATH = "some_path";

    @Test
    public void shouldCallDiskRepository() throws Exception {
        ArtifactRepository artifactRepository = new ArtifactRepository(diskFileStoreRepository, fileStoreJpaRepository);
        List<org.egov.filestore.domain.model.Artifact> listOfMockedArtifacts = getListOfArtifacts();
        artifactRepository.save(listOfMockedArtifacts);

        verify(diskFileStoreRepository).write(listOfMockedArtifacts);
    }

    @Test
    public void shouldCallFileStoreMapperRepository() throws Exception {
        ArtifactRepository artifactRepository = new ArtifactRepository(diskFileStoreRepository, fileStoreJpaRepository);
        List<org.egov.filestore.domain.model.Artifact> listOfMockedArtifacts = getListOfArtifacts();


        when(fileStoreJpaRepository.save(listArgumentCaptor.capture())).thenReturn(Arrays.asList());

        artifactRepository.save(listOfMockedArtifacts);

        assertEquals("filename1.extension", listArgumentCaptor.getValue().get(0).getFileName());
        assertEquals("filename2.extension", listArgumentCaptor.getValue().get(1).getFileName());
    }

    private List<org.egov.filestore.domain.model.Artifact> getListOfArtifacts() {
        MultipartFile multipartFile1 = mock(MultipartFile.class);
        MultipartFile multipartFile2 = mock(MultipartFile.class);

        when(multipartFile1.getOriginalFilename()).thenReturn("filename1.extension");
        when(multipartFile2.getOriginalFilename()).thenReturn("filename2.extension");

        return Arrays.asList(
                new org.egov.filestore.domain.model.Artifact(multipartFile1, UUID.randomUUID().toString(), MODULE, TENANT_ID, FILE_STORAGE_MOUNT_PATH),
                new org.egov.filestore.domain.model.Artifact(multipartFile2, UUID.randomUUID().toString(), MODULE, TENANT_ID, FILE_STORAGE_MOUNT_PATH)
        );
    }
}