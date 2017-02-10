package org.egov.filestore.persistence.repository;


import org.egov.filestore.domain.model.Artifact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DiskFileStoreRepositoryTest {

    @Mock
    FileRepository fileRepository;

    private final String FILE_STORAGE_MOUNT_PATH = "some_path";
    private final String TENANT_ID = "tenant_id";
    private final String MODULE = "module_id";

    @Test
    public void shouldStoreFileToDisk() throws Exception {
        DiskFileStoreRepository diskFileStoreRepository =
                new DiskFileStoreRepository(fileRepository);

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        String fileStoreId2 = UUID.randomUUID().toString();
        String fileStoreId1 = UUID.randomUUID().toString();
        List<Artifact> listOfMockedArtifacts = Arrays.asList(
                new Artifact(file1, fileStoreId1, MODULE, TENANT_ID, FILE_STORAGE_MOUNT_PATH),
                new Artifact(file2, fileStoreId2, MODULE, TENANT_ID, FILE_STORAGE_MOUNT_PATH)
        );

        diskFileStoreRepository.write(listOfMockedArtifacts);

        verify(fileRepository).write(file1, Paths.get(FILE_STORAGE_MOUNT_PATH, TENANT_ID, MODULE, fileStoreId1));
        verify(fileRepository).write(file2, Paths.get(FILE_STORAGE_MOUNT_PATH, TENANT_ID, MODULE, fileStoreId2));
    }

}