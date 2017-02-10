package org.egov.filestore.web.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.egov.filestore.domain.service.StorageService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StorageController.class)
public class StorageControllerTest {

    private static final String MODULE = "module";
    private static final String JURISDICTION_ID = "jurisdictionId";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;


    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockJpegImageFile = new MockMultipartFile("file", "this is an image.jpeg", "image/jpeg", "image content".getBytes());
        MockMultipartFile mockPdfDocumentFile = new MockMultipartFile("file", "lease_agreement.pdf", "application/pdf", "pdf content".getBytes());

        when(storageService.save(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), JURISDICTION_ID, MODULE)).thenReturn(Arrays.asList("fileStoreId1", "fileStoreId2"));

        mockMvc.perform(
            fileUpload("/files")
                .file(mockJpegImageFile)
                .file(mockPdfDocumentFile)
                .param("jurisdictionId", JURISDICTION_ID)
                .param("module", MODULE)
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson()));

        verify(storageService)
                .save(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), JURISDICTION_ID, MODULE);
    }

    @Test
    public void testDownloadFile() throws Exception {
        URL url = this.getClass().getClassLoader().getResource("hello.txt");
        Resource fileSystemResource = new FileSystemResource(FileUtils.toFile(url));

        org.egov.filestore.domain.model.Resource resource =
                new org.egov.filestore.domain.model.Resource("image/png", "image.png", fileSystemResource);

        when(storageService.retrieve("FileStoreId")).thenReturn(resource);

        mockMvc.perform(get("/files/FileStoreId"))
                .andExpect(content().contentType(resource.getContentType()))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.png\""))
                .andExpect(content().bytes(getExpectedBytes(fileSystemResource)));
    }

    private byte[] getExpectedBytes(Resource fileSystemResource) throws IOException {
        return IOUtils.toString(fileSystemResource.getInputStream(), "UTF-8").getBytes();
    }

    private String expectedJson() {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream("storageResponse.json"), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}