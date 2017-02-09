package org.egov.filestore.web.controller;

import org.egov.filestore.domain.service.FileStorageService;
import org.egov.filestore.persistence.entity.FileStoreMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    public void testGetFile() throws Exception {
        mockMvc.perform(get("/files")).andExpect(status().isOk());
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockJpegImageFile = new MockMultipartFile("file", "this is an image.jpeg", "image/jpeg", "image content".getBytes());
        MockMultipartFile mockPdfDocumentFile = new MockMultipartFile("file", "lease_agreement.pdf", "application/pdf", "pdf content".getBytes());

        when(fileStorageService.storeFiles(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), "mumbai", "pgr")).thenReturn(Arrays.asList(new FileStoreMapper()));

        mockMvc.perform(
            fileUpload("/files")
                .file(mockJpegImageFile)
                .file(mockPdfDocumentFile)
                .param("tenantId", "mumbai")
                .param("module", "pgr")
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json("[{\"version\":null,\"id\":null,\"fileStoreId\":null,\"fileName\":null,\"contentType\":null,\"new\":true}]"));

        verify(fileStorageService).storeFiles(Arrays.asList(mockJpegImageFile, mockPdfDocumentFile), "mumbai", "pgr");
    }
}