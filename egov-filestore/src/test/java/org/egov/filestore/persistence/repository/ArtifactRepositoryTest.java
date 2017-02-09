package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.entity.FileStoreMapper;
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
    FileStoreMapperRepository fileStoreMapperRepository;

    @Captor
    private ArgumentCaptor<List<FileStoreMapper>> listArgumentCaptor;

    private final String TENANT_ID = "mumbai";
    private final String MODULE = "pgr";

    @Test
    public void shouldCallDiskRepository() throws Exception {
        ArtifactRepository artifactRepository = new ArtifactRepository(diskFileStoreRepository, fileStoreMapperRepository);
        List<Artifact> listOfMockedArtifacts = getListOfArtifacts();
        artifactRepository.storeArtifacts(listOfMockedArtifacts, TENANT_ID, MODULE);

        verify(diskFileStoreRepository).storeFilesOnDisk(listOfMockedArtifacts, TENANT_ID, MODULE);
    }

    @Test
    public void shouldCallFileStoreMapperRepository() throws Exception {
        ArtifactRepository artifactRepository = new ArtifactRepository(diskFileStoreRepository, fileStoreMapperRepository);
        List<Artifact> listOfMockedArtifacts = getListOfArtifacts();


        when(fileStoreMapperRepository.save(listArgumentCaptor.capture())).thenReturn(Arrays.asList());

        artifactRepository.storeArtifacts(listOfMockedArtifacts, TENANT_ID, MODULE);

        assertEquals("filename1.extension", listArgumentCaptor.getValue().get(0).getFileName());
        assertEquals("filename2.extension", listArgumentCaptor.getValue().get(1).getFileName());
    }

    private List<Artifact> getListOfArtifacts() {
        MultipartFile multipartFile1 = mock(MultipartFile.class);
        MultipartFile multipartFile2 = mock(MultipartFile.class);

        when(multipartFile1.getOriginalFilename()).thenReturn("filename1.extension");
        when(multipartFile2.getOriginalFilename()).thenReturn("filename2.extension");

        return Arrays.asList(
                new Artifact(multipartFile1, UUID.randomUUID().toString()),
                new Artifact(multipartFile2, UUID.randomUUID().toString())
        );
    }
}