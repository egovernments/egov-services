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
import org.egov.models.LicenseStatus;
import org.egov.models.LicenseStatusRequest;
import org.egov.models.LicenseStatusResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.services.CategoryService;
import org.egov.tradelicense.services.DocumentTypeService;
import org.egov.tradelicense.services.FeeMatrixService;
import org.egov.tradelicense.services.LicenseStatusService;
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
public class LicenseStatusControllerTest {
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	FeeMatrixService feeMatrixService;

	@MockBean
	private UOMService uomService;

	@MockBean
	private PenaltyRateService penaltyRateService;
	
	@MockBean
	private LicenseStatusService licenseStatusService;
	
	@MockBean
	DocumentTypeService documentTypeService;
	
	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * Description : Test method for createLicenceStatus() method
	 */
	
	@Test
	public void testCreateLicenceStatus() throws Exception {

		List<LicenseStatus> LicenseStatulst = new ArrayList<>();
		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		licenseStatus.setAuditDetails(auditDetails);
		licenseStatus.setName("shubham");
		licenseStatus.setCode("babu");
		licenseStatus.setActive(true);

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		LicenseStatulst.add(licenseStatus);

		licenseStatusResponse.setResponseInfo(new ResponseInfo());
		licenseStatusResponse.setLicenseStatuses(LicenseStatulst);

		try {

			when(licenseStatusService.createLicenseStatusMaster(any(LicenseStatusRequest.class)))
			.thenReturn(licenseStatusResponse);

			mockMvc.perform(post("/tradelicense/status/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("LicenseStatusCreateRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("LicenseStatusCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}
	
	/**
	 * Description : Test method for updateLicenceStatus() method
	 */
	@Test
	public void testUpdateLicenceStatus() throws Exception {

		List<LicenseStatus> LicenseStatulst = new ArrayList<>();
		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		licenseStatus.setAuditDetails(auditDetails);
		licenseStatus.setName("shubham");
		licenseStatus.setCode("babu");
		licenseStatus.setActive(true);

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		LicenseStatulst.add(licenseStatus);

		licenseStatusResponse.setResponseInfo(new ResponseInfo());
		licenseStatusResponse.setLicenseStatuses(LicenseStatulst);

		try {

			when(licenseStatusService.updateLicenseStatusMaster(any(LicenseStatusRequest.class)))
			.thenReturn(licenseStatusResponse);

			mockMvc.perform(post("/tradelicense/status/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("LicenseStatusUpdateRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("LicenseStatusUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}
	
	/**
	 * Description : Test method for searchLicenceStatus() method
	 */
	@Test
	public void testSearchLicenceStatus() throws Exception {

		List<LicenseStatus> LicenseStatulst = new ArrayList<>();
		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		licenseStatus.setAuditDetails(auditDetails);
		licenseStatus.setName("shubham");
		licenseStatus.setCode("nitin");
		licenseStatus.setActive(true);

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		LicenseStatulst.add(licenseStatus);

		licenseStatusResponse.setResponseInfo(new ResponseInfo());
		licenseStatusResponse.setLicenseStatuses(LicenseStatulst);

		try {

			when(licenseStatusService.getLicenseStatusMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
							.thenReturn(licenseStatusResponse);

			mockMvc.perform(post("/tradelicense/status/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("LicenseStatusSearchRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("LicenseStatusSearchResponse.json")));

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
