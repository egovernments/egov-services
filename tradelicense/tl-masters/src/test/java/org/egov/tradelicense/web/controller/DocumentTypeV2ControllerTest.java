package org.egov.tradelicense.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.utils.RequestJsonReader;
import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.requests.DocumentTypeV2Request;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.masters.domain.service.DocumentTypeV2Service;
import org.egov.tl.masters.web.controller.DocumentTypeV2Controller;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.configuration.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentTypeV2Controller.class)
@Import(TestConfiguration.class)
public class DocumentTypeV2ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DocumentTypeV2Service documentTypeService;

	@MockBean
	ResponseInfoFactory responseInfoFactory;

	@MockBean
	PropertiesManager propertiesManager;

	private RequestJsonReader resources = new RequestJsonReader();

	@Captor
	private ArgumentCaptor<DocumentTypeV2Request> captor;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Description : Test method for createCategory() method
	 * 
	 */

	@Test
	public void testCreateDocumentTypeV2() throws IOException, Exception {

		try {

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("1");
			auditDetails.setLastModifiedBy("1");
			auditDetails.setCreatedTime(1503557894862L);
			auditDetails.setLastModifiedTime(null);
			List<DocumentType> documentTypes = new ArrayList<>();
			DocumentType documentType = new DocumentType();
			documentType.setId(4l);
			documentType.setCategory("1");
			documentType.setSubCategory("2");
			documentType.setApplicationType(ApplicationTypeEnum.fromValue("NEW"));
			documentType.setName("Environmen11");
			documentType.setMandatory(false);
			documentType.setEnabled(true);
			documentType.setTenantId("default");
			documentType.setAuditDetails(auditDetails);

			documentTypes.add(documentType);

			when(documentTypeService.add(anyListOf(DocumentType.class), any(RequestInfo.class)))
					.thenReturn(documentTypes);

			mockMvc.perform(
					post("/documenttype/v2/_create").content(resources.readRequest("documenttypeV2CreateRequest.json"))
							.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("documenttypeV2CreateResponse.json")));

			verify(documentTypeService).addToQue(captor.capture());

			final DocumentTypeV2Request actualRequest = captor.getValue();
			assertEquals(true, actualRequest.getDocumentTypes().get(0).getEnabled());

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	
	  @Test 
	  public void testUpdateDocumentTypeV2() throws IOException, Exception {
	  
	  try {
	  
	  AuditDetails auditDetails = new AuditDetails();
	  auditDetails.setCreatedBy(null); 
	  auditDetails.setLastModifiedBy(null);
	  auditDetails.setCreatedTime(null);
	  auditDetails.setLastModifiedTime(null); 
	  List<DocumentType> documentTypes= new ArrayList<>();
	  DocumentType documentType = new DocumentType();
	  documentType.setId(4l); documentType.setCategory("1");
	  documentType.setSubCategory("2");
	  documentType.setApplicationType(ApplicationTypeEnum.fromValue("NEW"));
	  documentType.setName("Environmen11"); documentType.setMandatory(false);
	  documentType.setEnabled(true); documentType.setTenantId("default");
	  documentType.setAuditDetails(auditDetails);
	  
	  documentTypes.add(documentType);
	  
	  when(documentTypeService.add(anyListOf(DocumentType.class),
	  any(RequestInfo.class))).thenReturn(documentTypes);
	  
	  mockMvc.perform(post("/documenttype/v2/_update").content(resources.
	  readRequest("documenttypeV2UpdateRequest.json"))
	  .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
	  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	  .andExpect(content().json(getFileContents(
	  "documenttypeV2UpdateResponse.json")));
	  
	  verify(documentTypeService).updateToQue(captor.capture());
	  
	  final DocumentTypeV2Request actualRequest = captor.getValue();
	  assertEquals(true, actualRequest.getDocumentTypes().get(0).getEnabled());
	  
	  } catch (Exception e) {
	  
	  assertTrue(Boolean.FALSE); }
	  
	 assertTrue(Boolean.TRUE);
	  
	  }
	 

	@Test
	public void testSearchDocumenttype() throws Exception {

		try {

			org.egov.tl.commons.web.contract.AuditDetails auditDetails = new org.egov.tl.commons.web.contract.AuditDetails();

			List<DocumentType> documentTypes = new ArrayList<>();

			DocumentType documentType = new DocumentType();
			documentType.setTenantId("default");
			documentType.setApplicationType(ApplicationTypeEnum.fromValue("NEW"));
			documentType.setAuditDetails(auditDetails);

			documentTypes.add(documentType);

			when(documentTypeService.search(any(RequestInfo.class), any(String.class), any(Integer[].class), 
					any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
					any(String.class), any(Integer.class), any(Integer.class), any(Boolean.class))).thenReturn(documentTypes);

			mockMvc.perform(post("/documenttype/v2/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("documentTypeV2SearchRequest.json"))).andExpect(status().isOk())
					.andExpect(content().json(getFileContents("documentTypeV2SearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}
