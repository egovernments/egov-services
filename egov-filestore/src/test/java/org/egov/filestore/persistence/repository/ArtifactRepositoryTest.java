package org.egov.filestore.persistence.repository;

import org.egov.filestore.domain.model.Resource;
import org.egov.filestore.persistence.entity.Artifact;
import org.junit.Before;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactRepositoryTest {

    @Mock
    DiskFileStoreRepository diskFileStoreRepository;

    @Mock
    FileStoreJpaRepository fileStoreJpaRepository;

    @Captor
    private ArgumentCaptor<List<Artifact>> listArgumentCaptor;

    private final String JURISDICTION_ID = "jurisdictionId";
    private final String MODULE = "module";
    private ArtifactRepository artifactRepository;

    @Before
    public void setUp() {
        artifactRepository = new ArtifactRepository(diskFileStoreRepository, fileStoreJpaRepository);
    }

    @Test
    public void shouldSaveArtifactToRepository() throws Exception {
        List<org.egov.filestore.domain.model.Artifact> listOfMockedArtifacts = getListOfArtifacts();

        artifactRepository.save(listOfMockedArtifacts);

        verify(diskFileStoreRepository).write(listOfMockedArtifacts);
    }

    @Test
    public void shouldPersistArtifactMetaDataToJpaRepository() throws Exception {
        List<org.egov.filestore.domain.model.Artifact> listOfMockedArtifacts = getListOfArtifacts();


        when(fileStoreJpaRepository.save(listArgumentCaptor.capture())).thenReturn(Arrays.asList());

        artifactRepository.save(listOfMockedArtifacts);

        assertEquals("filename1.extension", listArgumentCaptor.getValue().get(0).getFileName());
        assertEquals("image/png", listArgumentCaptor.getValue().get(0).getContentType());
        assertEquals(JURISDICTION_ID, listArgumentCaptor.getValue().get(0).getJurisdictionId());
        assertEquals(MODULE, listArgumentCaptor.getValue().get(0).getModule());
        assertEquals("filename2.extension", listArgumentCaptor.getValue().get(1).getFileName());

    }

    @Test
    public void shouldRetrieveArtifactMetaDataForGivenFileStoreId() {
        org.springframework.core.io.Resource mockedResource = mock(org.springframework.core.io.Resource.class);
        when(diskFileStoreRepository.read(any())).thenReturn(mockedResource);
        Artifact artifact = new Artifact();
        artifact.setFileStoreId("fileStoreId");
        artifact.setContentType("contentType");
        artifact.setFileName("fileName");
        when(fileStoreJpaRepository.findByFileStoreId("fileStoreId")).thenReturn(artifact);

        Resource actualResource = artifactRepository.find("fileStoreId");

        assertEquals(actualResource.getContentType(), "contentType");
        assertEquals(actualResource.getFileName(), "fileName");
        assertEquals(actualResource.getResource(), mockedResource);
    }

    private List<org.egov.filestore.domain.model.Artifact> getListOfArtifacts() {
        MultipartFile multipartFile1 = mock(MultipartFile.class);
        MultipartFile multipartFile2 = mock(MultipartFile.class);

        when(multipartFile1.getOriginalFilename()).thenReturn("filename1.extension");
        when(multipartFile1.getContentType()).thenReturn("image/png");
        when(multipartFile2.getOriginalFilename()).thenReturn("filename2.extension");

        return Arrays.asList(
                new org.egov.filestore.domain.model.Artifact(multipartFile1, UUID.randomUUID().toString(), MODULE, JURISDICTION_ID),
                new org.egov.filestore.domain.model.Artifact(multipartFile2, UUID.randomUUID().toString(), MODULE, JURISDICTION_ID)
        );
    }
}