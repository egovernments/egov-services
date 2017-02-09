package org.egov.filestore.domain.service;


import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.entity.FileStoreMapper;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTest {

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    @Captor
    private ArgumentCaptor<List<FileStoreMapper>> listArgumentCaptor;

    private final String TENANT_ID = "mumbai";
    private final String MODULE = "pgr";

    @Test
    public void shouldCallArtifactRepo() throws Exception {
        FileStorageService fileStorageService = new FileStorageService(artifactRepository, idGeneratorService);
        List<MultipartFile> listOfMultipartFiles = getMockFileList();
        List<Artifact> listOfArtifacts = getArtifactList(listOfMultipartFiles);

        when(idGeneratorService.getId()).thenReturn("FileStoreID1", "FileStoreID2");

        fileStorageService.storeFiles(listOfMultipartFiles, TENANT_ID, MODULE);

        verify(artifactRepository).storeArtifacts(listOfArtifacts, TENANT_ID, MODULE);
    }

    private List<MultipartFile> getMockFileList() {
        MultipartFile multipartFile1 = new MockMultipartFile("file", "filename1.extension", "mime type", "content".getBytes());
        MultipartFile multipartFile2 = new MockMultipartFile("file", "filename2.extension", "mime type", "content".getBytes());

        return Arrays.asList(multipartFile1, multipartFile2);
    }

    private List<Artifact> getArtifactList(List<MultipartFile> multipartFiles) {
        Artifact artifact1 = new Artifact(multipartFiles.get(0), "FileStoreID1");
        Artifact artifact2 = new Artifact(multipartFiles.get(1), "FileStoreID2");

        return Arrays.asList(artifact1, artifact2);
    }
}