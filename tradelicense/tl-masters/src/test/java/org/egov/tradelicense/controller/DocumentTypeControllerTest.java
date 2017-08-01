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

import org.egov.models.AuditDetails;
import org.egov.models.DocumentType;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.services.CategoryService;
import org.egov.tradelicense.services.DocumentTypeService;
import org.egov.tradelicense.services.FeeMatrixService;
import org.egov.tradelicense.services.PenaltyRateService;
import org.egov.tradelicense.services.UOMService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TradeLicenseMasterController.class)
@ContextConfiguration(classes = { TradeLicenseApplication.class })
public class DocumentTypeControllerTest {

	@MockBean
	private CategoryService categoryService;

	@MockBean
	FeeMatrixService feeMatrixService;

	@MockBean
	private UOMService uomService;

	@MockBean
	private PenaltyRateService penaltyRateService;

	@MockBean
	DocumentTypeService documentTypeService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

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

			when(documentTypeService.createDocumentType(any(DocumentTypeRequest.class)))
					.thenReturn(documentTypeResponse);

			mockMvc.perform(post("/tradelicense/documenttype/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("documentTypeCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("documentTypeCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testUpdateDocumentType() throws Exception {

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

			when(documentTypeService.updateDocumentType(any(DocumentTypeRequest.class)))
					.thenReturn(documenttypeResponse);
			mockMvc.perform(post("/tradelicense/documenttype/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("documenttypeUpdateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("documenttypeUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

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

			when(documentTypeService.getDocumentType(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(Boolean.class), any(String.class), any(Integer.class), any(Integer.class)))
							.thenReturn(documentTypeResponse);

			mockMvc.perform(post("/tradelicense/documenttype/_search").param("tenantId", "default")
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