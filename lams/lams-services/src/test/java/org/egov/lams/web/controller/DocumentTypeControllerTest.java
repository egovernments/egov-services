package org.egov.lams.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.TestConfiguration;
import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.egov.lams.service.DocumentTypeService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentTypeController.class)
@Import(TestConfiguration.class)
public class DocumentTypeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DocumentTypeService documentTypeService;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Test
	public void test_Should_Search_Documents() throws Exception{
		
		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("1");
		documentTypes.add(documentType);
		documentTypes.add(documentType);
		ResponseInfo responseInfo = new ResponseInfo();
		when(documentTypeService.getDocumentTypes(any(DocumentType.class))).thenReturn(documentTypes); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);

		mockMvc.perform(post("/agreements/document/_search")
	        		.param("tenantId","1")
	        		.param("appilcationtype",Application.CREATE.toString())
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("documenttypesearchresponse.json")));
	}
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}

}
