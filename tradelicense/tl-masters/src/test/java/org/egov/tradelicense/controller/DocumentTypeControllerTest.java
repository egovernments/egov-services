package org.egov.tradelicense.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.DocumentTypeRequest;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.DocumentTypeService;
import org.egov.tradelicense.web.controller.DocumentTypeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentTypeController.class)
@ContextConfiguration(classes = { TradeLicenseApplication.class })
public class DocumentTypeControllerTest {

	@MockBean
	DocumentTypeService documentTypeService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KafkaTemplate kafkaTemplate;

	/**
	 * Description : Test method for createDocumentType() method
	 */
	@Test
	public void testCreateDocumentType() throws Exception {

		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		documentTypes.add(documentType);

		documentTypeResponse.setResponseInfo(new ResponseInfo());
		documentTypeResponse.setDocumentTypes(documentTypes);

		try {

			when(documentTypeService.createDocumentTypeMaster(any(DocumentTypeRequest.class)))
					.thenReturn(documentTypeResponse);

			mockMvc.perform(post("/documenttype/v1/_create")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("documentTypeCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("documentTypeCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for updateDocumentType() method
	 */
	@Test
	public void testUpdateDocumentTypeMaster() throws Exception {

		DocumentTypeResponse documenttypeResponse = new DocumentTypeResponse();
		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		documentTypes.add(documentType);

		documenttypeResponse.setResponseInfo(new ResponseInfo());
		documenttypeResponse.setDocumentTypes(documentTypes);

		try {

			when(documentTypeService.updateDocumentTypeMaster(any(DocumentTypeRequest.class)))
					.thenReturn(documenttypeResponse);
			mockMvc.perform(post("/documenttype/v1/_update")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("documenttypeUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("documenttypeUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for searchDocumentType() method
	 */
	@Test
	public void testSearchDocumentType() throws Exception {

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		documentTypes.add(documentType);

		documentTypeResponse.setResponseInfo(new ResponseInfo());
		documentTypeResponse.setDocumentTypes(documentTypes);

		try {

			when(documentTypeService.getDocumentTypeMaster(any(RequestInfo.class), any(String.class),
					any(Integer[].class), any(String.class), any(String.class), any(String.class), any(Integer.class),
					any(Integer.class))).thenReturn(documentTypeResponse);

			mockMvc.perform(post("/documenttype/v1/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("documentTypeSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("documentTypeSearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}