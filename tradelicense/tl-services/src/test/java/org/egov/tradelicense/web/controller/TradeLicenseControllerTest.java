package org.egov.tradelicense.web.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.TlServicesApplication;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.web.contract.LicenseFeeDetailContract;
import org.egov.tradelicense.web.contract.SupportDocumentContract;
import org.egov.tradelicense.web.contract.TradeLicenseContract;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
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
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(TradeLicenseController.class)
@ContextConfiguration(classes = { TlServicesApplication.class })
public class TradeLicenseControllerTest {

	@MockBean
	TradeLicenseService tradeLicenseService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KafkaTemplate kafkaTemplate;

	/**
	 * Description : Test method for createCategory() method
	 * 
	 */

	@Test
	public void testCreateLegacyTrade() throws Exception {

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

			mockMvc.perform(post("/license/v1/_create").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("legacyTradeCreateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("legacyTradeCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchLegacyTrade() throws Exception {

		try {

			TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();

			ResponseInfo responseInfo = new ResponseInfo();

			AuditDetails auditDetails = new AuditDetails();

			List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<>();

			List<LicenseFeeDetailContract> feeDetails = new ArrayList<>();
			LicenseFeeDetailContract licenseFeeDetailContract = new LicenseFeeDetailContract();
			feeDetails.add(licenseFeeDetailContract);

			List<SupportDocumentContract> supportDocuments = new ArrayList<>();
			SupportDocumentContract supportDocument = new SupportDocumentContract();
			supportDocuments.add(supportDocument);

			TradeLicenseContract tradeLicenseContract = new TradeLicenseContract();
			tradeLicenseContract.setTenantId("default");
			tradeLicenseContract.setFeeDetails(feeDetails);
			tradeLicenseContract.setAuditDetails(auditDetails);

			tradeLicenseContracts.add(tradeLicenseContract);

			tradeLicenseResponse.setResponseInfo(responseInfo);

			tradeLicenseResponse.setLicenses(tradeLicenseContracts);

			when(tradeLicenseService.getTradeLicense(any(RequestInfo.class), any(String.class), any(Integer.class),
					any(Integer.class), any(String.class), any(String.class), any(String.class), any(String.class),
					any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
					any(Integer.class), any(Integer.class), any(String.class), any(String.class), any(String.class),
					any(Integer.class), any(Integer.class), any(String.class), any(Integer.class)))
							.thenReturn(tradeLicenseResponse);

			mockMvc.perform(post("/license/v1/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("legacyTradeSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().json(getFileContents("legacyTradeSearchResponse.json")));

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
