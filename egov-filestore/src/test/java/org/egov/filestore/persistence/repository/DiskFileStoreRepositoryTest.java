package org.egov.filestore.persistence.repository;


import org.egov.filestore.domain.model.Artifact;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiskFileStoreRepositoryTest {

    private final String FILE_STORAGE_MOUNT_PATH = "~";
    private final String TENANT_ID = "mumbai";
    private final String MODULE = "pgr";

    @Test
    @Ignore
    public void shouldStoreFileToDisk() throws Exception {
        DiskFileStoreRepository diskFileStoreRepository = new DiskFileStoreRepository(FILE_STORAGE_MOUNT_PATH);

        List<Artifact> listOfMockedArtifacts = getListOfArtifacts();

        diskFileStoreRepository.storeFilesOnDisk(listOfMockedArtifacts, TENANT_ID, MODULE);

        verify(listOfMockedArtifacts.get(0).getMultipartFile()).transferTo(new File(FILE_STORAGE_MOUNT_PATH));
        verify(listOfMockedArtifacts.get(1).getMultipartFile()).transferTo(new File(FILE_STORAGE_MOUNT_PATH));
    }

    private List<Artifact> getListOfArtifacts() {
        return Arrays.asList(
                new Artifact(mock(MultipartFile.class), UUID.randomUUID().toString()),
                new Artifact(mock(MultipartFile.class), UUID.randomUUID().toString())
        );
    }
}