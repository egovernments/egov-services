package org.egov.wcms.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.util.FileUtils;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentTypeApplicationTypeControllerTest.class)
@WebAppConfiguration
public class DocumentTypeApplicationTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private DocumentTypeApplicationTypeService docNameService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @Test(expected = Exception.class)
    public void test_Should_Search_PropertyCategory() throws Exception {

        final List<DocumentTypeApplicationType> docNames = new ArrayList<>();
        final RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        final DocumentTypeApplicationType docName = new DocumentTypeApplicationType();
        docName.setActive(true);
        docName.setApplicationType("Additional Connection");
        docName.setDocumentTypeId(123);
        docName.setTenantId("1");

        docNames.add(docName);

        final DocumentTypeApplicationTypeGetRequest docNameGetRequest = Mockito.mock(DocumentTypeApplicationTypeGetRequest.class);

        when(docNameService.getDocumentAndApplicationTypes(docNameGetRequest)).thenReturn(docNames);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(post("/documentName/_search")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("RequestInfo.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("propertycategoryresponse.json")));
    }

    private String getFileContents(final String filePath) throws IOException {
        return new FileUtils().getFileContents(
                "org/egov/wcms/web/controller/DocumentNamesController/" + filePath);
    }
}