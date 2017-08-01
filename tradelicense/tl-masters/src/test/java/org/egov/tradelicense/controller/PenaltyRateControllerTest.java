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
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.PenaltyRateResponse;
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
public class PenaltyRateControllerTest {

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
	public void testCreatePenaltyRate() throws Exception {

		List<PenaltyRate> penaltyRates = new ArrayList<>();
		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		penaltyRate.setAuditDetails(auditDetails);

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		penaltyRates.add(penaltyRate);

		penaltyRateResponse.setResponseInfo(new ResponseInfo());
		penaltyRateResponse.setPenaltyRates(penaltyRates);

		try {

			when(penaltyRateService.createPenaltyRateMaster(any(String.class), any(PenaltyRateRequest.class)))
					.thenReturn(penaltyRateResponse);

			mockMvc.perform(post("/tradelicense/penaltyrate/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("penaltyRateCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("penaltyRateCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testUpdatePenaltyRate() throws Exception {

		List<PenaltyRate> penaltyRates = new ArrayList<>();
		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		penaltyRate.setAuditDetails(auditDetails);

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		penaltyRates.add(penaltyRate);

		penaltyRateResponse.setResponseInfo(new ResponseInfo());
		penaltyRateResponse.setPenaltyRates(penaltyRates);

		try {

			when(penaltyRateService.updatePenaltyRateMaster(any(PenaltyRateRequest.class)))
					.thenReturn(penaltyRateResponse);
			mockMvc.perform(post("/tradelicense/penaltyrate/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("penaltyRateUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("penaltyRateUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchPenaltyRate() throws Exception {

		List<PenaltyRate> penaltyRates = new ArrayList<>();
		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		penaltyRate.setAuditDetails(auditDetails);

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		penaltyRates.add(penaltyRate);

		penaltyRateResponse.setResponseInfo(new ResponseInfo());
		penaltyRateResponse.setPenaltyRates(penaltyRates);

		try {

			when(penaltyRateService.getPenaltyRateMaster(any(RequestInfo.class), any(String.class),
					any(Integer[].class), any(String.class), any(Integer.class), any(Integer.class)))
							.thenReturn(penaltyRateResponse);

			mockMvc.perform(post("/tradelicense/penaltyrate/_search").param("tenantId", "default")
					.param("applicationType", "New").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("penaltyRateSearchRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("penaltyRateSearchResponse.json")));

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