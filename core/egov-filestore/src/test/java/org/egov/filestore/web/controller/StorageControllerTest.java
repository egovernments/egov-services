package org.egov.filestore.web.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.egov.filestore.domain.exception.ArtifactNotFoundException;
import org.egov.filestore.domain.model.FileInfo;
import org.egov.filestore.domain.service.StorageService;
import org.egov.filestore.web.contract.FileRecord;
import org.egov.filestore.web.contract.GetFilesByTagResponse;
import org.egov.filestore.web.contract.ResponseFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StorageController.class)
public class StorageControllerTest {

    private static final String MODULE = "module";
    private static final String JURISDICTION_ID = "jurisdictionId";
    private static final String TAG = "tag";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @MockBean
    private ResponseFactory responseFactory;

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockJpegImageFile = new MockMultipartFile("file", "this is an image.jpeg", "image/jpeg", "image content".getBytes());
        MockMultipartFile mockPdfDocumentFile = new MockMultipartFile("file", "lease_agreement.pdf", "application/pdf", "pdf content".getBytes());

        when(storageService.save(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), JURISDICTION_ID, MODULE, TAG)).thenReturn(Arrays.asList("fileStoreId1", "fileStoreId2"));

        mockMvc.perform(
                fileUpload("/v1/files")
                        .file(mockJpegImageFile)
                        .file(mockPdfDocumentFile)
                        .param("jurisdictionId", JURISDICTION_ID)
                        .param("module", MODULE)
                        .param("tag", TAG)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getStorageResponse()));

        verify(storageService)
                .save(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), JURISDICTION_ID, MODULE, TAG);
    }

    @Test
    public void testDownloadFile() throws Exception {
        URL url = this.getClass().getClassLoader().getResource("hello.txt");
        Resource fileSystemResource = new FileSystemResource(FileUtils.toFile(url));

        org.egov.filestore.domain.model.Resource resource =
                new org.egov.filestore.domain.model.Resource("image/png", "image.png", fileSystemResource);

        when(storageService.retrieve("FileStoreId")).thenReturn(resource);

        mockMvc.perform(get("/v1/files/id").param("fileStoreId", "FileStoreId"))
                .andExpect(content().contentType(resource.getContentType()))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.png\""))
                .andExpect(content().bytes(getExpectedBytes(fileSystemResource)));
    }

    @Test
    public void testRetrievingFilesListByTag() throws Exception {
        FileRecord fileRecord1 = new FileRecord("/filestore/v1/files/id?fileStoreId=fileStoreId1",
                "image/png");
        FileRecord fileRecord2 = new FileRecord("/filestore/v1/files/id?fileStoreId=fileStoreId2",
                "application/pdf");

        GetFilesByTagResponse getFilesByTagResponse = new GetFilesByTagResponse(asList(fileRecord1, fileRecord2));

        List<FileInfo> fileInfoList = asList(mock(FileInfo.class), mock(FileInfo.class));

        when(storageService.retrieveByTag(TAG)).thenReturn(fileInfoList);
        when(responseFactory.getFilesByTagResponse(fileInfoList)).thenReturn(getFilesByTagResponse);

        mockMvc.perform(
                get("/v1/files/tag").param("tag", TAG)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getRetrieveByTagResponse()));

        verify(storageService).retrieveByTag(TAG);
    }

    @Test
    public void test404WhenFileIsNotFound() throws Exception {
        when(storageService.retrieve("fileStoreId")).thenThrow(new ArtifactNotFoundException("fileStoreId"));

        mockMvc.perform(get("/v1/files/id").param("fileStoreId", "fileStoreId")).andExpect(status().isNotFound());
    }

    private byte[] getExpectedBytes(Resource fileSystemResource) throws IOException {
        return IOUtils.toString(fileSystemResource.getInputStream(), "UTF-8").getBytes();
    }

    private String getStorageResponse() {
        return getFileContents("storageResponse.json");
    }

    private String getRetrieveByTagResponse() {
        return getFileContents("retrieveByTagResponse.json");
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}