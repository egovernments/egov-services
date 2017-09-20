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
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.configuration.TestConfiguration;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.repository.TradeLicenseSearchContractRepository;
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
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(TradeLicenseController.class)
@Import(TestConfiguration.class)
public class TradeLicenseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TradeLicenseSearchContractRepository tradeLicenseSearchContractRepository;
	
	@MockBean
	TradeLicenseService tradeLicenseService;

	@MockBean
	TradeLicenseServiceValidator tradeLicenseServiceValidator;

	@MockBean
	PropertiesManager propertiesManager;

	private RequestJsonReader resources = new RequestJsonReader();

	@Captor
	private ArgumentCaptor<TradeLicenseRequest> captor;

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
	public void testCreateLegacyTrade() throws IOException, Exception {

		try {

			AuditDetails auditDetails = new AuditDetails();

			List<TradeLicense> licenses = new ArrayList<>();

			List<LicenseFeeDetail> feeDetails = new ArrayList<>();
			LicenseFeeDetail licenseFeeDetail = new LicenseFeeDetail();
			feeDetails.add(licenseFeeDetail);

			List<SupportDocument> supportDocuments = new ArrayList<>();
			SupportDocument supportDocument = new SupportDocument();
			supportDocuments.add(supportDocument);

			TradeLicense tradeLicense = new TradeLicense();
			tradeLicense.setFeeDetails(feeDetails);
			tradeLicense.setAuditDetails(auditDetails);

			licenses.add(tradeLicense);

			when(tradeLicenseService.add(anyListOf(TradeLicense.class), any(RequestInfo.class),
					any(BindingResult.class))).thenReturn(licenses);

			mockMvc.perform(post("/license/v1/_create").content(resources.readRequest("legacyTradeCreateRequest.json"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("legacyTradeCreateResponse.json")));

			verify(tradeLicenseService).addToQue(captor.capture(), any(Boolean.class));

			final TradeLicenseRequest actualRequest = captor.getValue();
			assertEquals(true, actualRequest.getLicenses().get(0).getActive());

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/*
	 * @Test public void testSearchLegacyTrade() throws Exception {
	 * 
	 * try {
	 * 
	 * TradeLicenseSearchResponse tradeLicenseSearchResponse = new
	 * TradeLicenseSearchResponse();
	 * 
	 * ResponseInfo responseInfo = new ResponseInfo();
	 * 
	 * org.egov.tl.commons.web.contract.AuditDetails auditDetails = new
	 * org.egov.tl.commons.web.contract.AuditDetails();
	 * 
	 * List<TradeLicenseSearchContract> tradeLicenseSearchContracts = new
	 * ArrayList<>();
	 * 
	 * List<LicenseFeeDetailContract> feeDetails = new ArrayList<>();
	 * LicenseFeeDetailContract licenseFeeDetailContract = new
	 * LicenseFeeDetailContract(); feeDetails.add(licenseFeeDetailContract);
	 * 
	 * List<SupportDocumentContract> supportDocuments = new ArrayList<>();
	 * SupportDocumentContract supportDocument = new SupportDocumentContract();
	 * supportDocuments.add(supportDocument);
	 * 
	 * TradeLicenseSearchContract tradeLicenseSearchContract = new
	 * TradeLicenseSearchContract();
	 * tradeLicenseSearchContract.setTenantId("default");
	 * tradeLicenseSearchContract.setFeeDetails(feeDetails);
	 * tradeLicenseSearchContract.setAuditDetails(auditDetails);
	 * 
	 * tradeLicenseSearchContracts.add(tradeLicenseSearchContract);
	 * 
	 * tradeLicenseSearchResponse.setResponseInfo(responseInfo);
	 * 
	 * tradeLicenseSearchResponse.setLicenses(tradeLicenseSearchContracts);
	 * 
	 * when(tradeLicenseService.getTradeLicense(any(RequestInfo.class),
	 * any(String.class), any(Integer.class), any(Integer.class),
	 * any(String.class), any(String.class), any(Integer[].class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class), any(Integer.class), any(Integer.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(Integer.class), any(Integer.class), any(String.class),
	 * any(Integer.class), any(Integer.class)))
	 * .thenReturn(tradeLicenseSearchResponse);
	 * 
	 * mockMvc.perform(post("/license/v1/_search").param("tenantId", "default")
	 * .contentType(MediaType.APPLICATION_JSON).content(getFileContents(
	 * "legacyTradeSearchRequest.json"))) .andExpect(status().isOk())
	 * .andExpect(content().json(getFileContents(
	 * "legacyTradeSearchResponse.json")));
	 * 
	 * } catch (Exception e) {
	 * 
	 * assertTrue(Boolean.FALSE); }
	 * 
	 * assertTrue(Boolean.TRUE);
	 * 
	 * }
	 */

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}
