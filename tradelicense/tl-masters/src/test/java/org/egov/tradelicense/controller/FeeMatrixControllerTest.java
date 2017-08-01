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
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.models.FeeMatrixRequest;
import org.egov.models.FeeMatrixResponse;
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
public class FeeMatrixControllerTest {

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
	public void testCreateFeeMatrix() throws Exception {

		List<FeeMatrix> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId("default");
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrices.add(feeMatrix);

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		feeMatrixResponse.setResponseInfo(new ResponseInfo());
		feeMatrixResponse.setFeeMatrices(feeMatrices);

		try {

			when(feeMatrixService.createFeeMatrixMaster(any(String.class), any(FeeMatrixRequest.class)))
					.thenReturn(feeMatrixResponse);

			mockMvc.perform(post("/tradelicense/feematrix/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("feeMatrixCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testUpdateFeeMatrix() throws Exception {

		List<FeeMatrix> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId("default");
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrices.add(feeMatrix);

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		feeMatrixResponse.setResponseInfo(new ResponseInfo());
		feeMatrixResponse.setFeeMatrices(feeMatrices);

		try {

			when(feeMatrixService.updateFeeMatrixMaster(any(FeeMatrixRequest.class))).thenReturn(feeMatrixResponse);

			mockMvc.perform(post("/tradelicense/feematrix/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("feeMatrixUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testSearchFeeMatrix() throws Exception {

		List<FeeMatrix> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId("default");
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrices.add(feeMatrix);

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		feeMatrixResponse.setResponseInfo(new ResponseInfo());
		feeMatrixResponse.setFeeMatrices(feeMatrices);

		try {

			when(feeMatrixService.getFeeMatrixMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(Integer.class), any(Integer.class), any(String.class), any(String.class), any(String.class),
					any(Integer.class), any(Integer.class))).thenReturn(feeMatrixResponse);

			mockMvc.perform(post("/tradelicense/feematrix/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("feeMatrixSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixSearchResponse.json")));

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