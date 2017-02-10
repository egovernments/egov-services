package org.egov.filestore.domain.service;


import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.persistence.repository.ArtifactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTest {

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    private final String MODULE = "pgr";
    private final String MOUNT_PATH = "mountPath";
    private final String JURISDICTION_ID = "mumbai";
    private final String FILE_STORE_ID_1 = "FileStoreID1";
    private final String FILE_STORE_ID_2 = "FileStoreID2";

    @Test
    public void shouldSaveArtifacts() throws Exception {
        StorageService storageService = new StorageService(artifactRepository, idGeneratorService, MOUNT_PATH);
        List<MultipartFile> listOfMultipartFiles = getMockFileList();
        List<Artifact> listOfArtifacts = getArtifactList(listOfMultipartFiles);

        when(idGeneratorService.getId()).thenReturn(FILE_STORE_ID_1, FILE_STORE_ID_2);

        storageService.save(listOfMultipartFiles, JURISDICTION_ID, MODULE);

        verify(artifactRepository).save(listOfArtifacts);
    }

    private List<MultipartFile> getMockFileList() {
        MultipartFile multipartFile1 = new MockMultipartFile("file", "filename1.extension",
                "mime type", "content".getBytes());
        MultipartFile multipartFile2 = new MockMultipartFile("file", "filename2.extension",
                "mime type", "content".getBytes());

        return Arrays.asList(multipartFile1, multipartFile2);
    }

    private List<Artifact> getArtifactList(List<MultipartFile> multipartFiles) {
        Artifact artifact1 = new Artifact(multipartFiles.get(0), FILE_STORE_ID_1, MODULE, JURISDICTION_ID, MOUNT_PATH);
        Artifact artifact2 = new Artifact(multipartFiles.get(1), FILE_STORE_ID_2, MODULE, JURISDICTION_ID, MOUNT_PATH);

        return Arrays.asList(artifact1, artifact2);
    }
}