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

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
import org.egov.tl.masters.domain.model.FeeMatrixSearchCriteria;
import org.egov.tl.masters.domain.service.FeeMatrixService;
import org.egov.tl.masters.web.controller.FeeMatrixController;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
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
@WebMvcTest(FeeMatrixController.class)
@ContextConfiguration(classes = { TradeLicenseApplication.class })
public class FeeMatrixControllerTest {

	@MockBean
	FeeMatrixService feeMatrixService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * Description : Test method for createFeeMatrix() method
	 */
	@Test
	public void testCreateFeeMatrix() throws Exception {

		List<FeeMatrix> feeMatrices = new ArrayList<>();
		FeeMatrix feeMatrix = new FeeMatrix();

		feeMatrix.setId(1l);
		feeMatrix.setTenantId("default");
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue("NEW"));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue("PERMANENT"));
		feeMatrix.setCategory("flammables");
		feeMatrix.setSubCategory("crackers");
		feeMatrix.setFinancialYear("2");
		feeMatrix.setFeeType(FeeTypeEnum.fromValue("LICENSE"));

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail1 = new FeeMatrixDetail();

		feeMatrixDetail1.setId(1l);
		feeMatrixDetail1.setTenantId("default");
		feeMatrixDetail1.setUomFrom(0l);
		feeMatrixDetail1.setUomTo(10l);
		feeMatrixDetail1.setAmount(100.00);

		FeeMatrixDetail feeMatrixDetail2 = new FeeMatrixDetail();

		feeMatrixDetail2.setId(2l);
		feeMatrixDetail2.setTenantId("default");
		feeMatrixDetail2.setUomFrom(10l);
		feeMatrixDetail2.setAmount(200.00);

		feeMatrixDetails.add(feeMatrixDetail1);
		feeMatrixDetails.add(feeMatrixDetail2);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		feeMatrices.add(feeMatrix);

		try {

			when(feeMatrixService.createFeeMatrixMaster(anyListOf(FeeMatrix.class), any(RequestInfo.class)))
					.thenReturn(feeMatrices);

			mockMvc.perform(post("/feematrix/v1/_create").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("feeMatrixCreateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixCreateResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	/**
	 * Description : Test method for updateFeeMatrix() method
	 */
	@Test
	public void testUpdateFeeMatrix() throws Exception {

		List<FeeMatrix> feeMatrices = new ArrayList<>();
		FeeMatrix feeMatrix = new FeeMatrix();

		feeMatrix.setId(1l);
		feeMatrix.setTenantId("default");
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue("NEW"));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue("PERMANENT"));
		feeMatrix.setCategory("flammables");
		feeMatrix.setSubCategory("crackers");
		feeMatrix.setFinancialYear("2");
		feeMatrix.setFeeType(FeeTypeEnum.fromValue("LICENSE"));

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail1 = new FeeMatrixDetail();

		feeMatrixDetail1.setId(1l);
		feeMatrixDetail1.setTenantId("default");
		feeMatrixDetail1.setUomFrom(0l);
		feeMatrixDetail1.setUomTo(20l);
		feeMatrixDetail1.setAmount(150.00);

		FeeMatrixDetail feeMatrixDetail2 = new FeeMatrixDetail();

		feeMatrixDetail2.setId(2l);
		feeMatrixDetail2.setTenantId("default");
		feeMatrixDetail2.setUomFrom(20l);
		feeMatrixDetail2.setUomTo(30l);
		feeMatrixDetail2.setAmount(250.00);

		feeMatrixDetails.add(feeMatrixDetail1);
		feeMatrixDetails.add(feeMatrixDetail2);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		feeMatrices.add(feeMatrix);

		try {

			when(feeMatrixService.updateFeeMatrixMaster(anyListOf(FeeMatrix.class), any(RequestInfo.class)))
					.thenReturn(feeMatrices);

			mockMvc.perform(post("/feematrix/v1/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("feeMatrixUpdateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	/**
	 * Description : Test method for searchFeeMatrix() method
	 */
	@Test
	public void testSearchFeeMatrix() throws Exception {

		List<FeeMatrixSearch> feeMatrixSearch = new ArrayList<>();
		FeeMatrixSearch feeMatrix = new FeeMatrixSearch();

		feeMatrix.setId(1l);
		feeMatrix.setTenantId("default");
		feeMatrix.setApplicationType("NEW");
		feeMatrix.setBusinessNature("PERMANENT");
		feeMatrix.setCategory("flammables");
		feeMatrix.setSubCategory("crackers");
		feeMatrix.setFinancialYear("2");
		feeMatrix.setFeeType("LICENSE");

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail1 = new FeeMatrixDetail();
		FeeMatrixDetail feeMatrixDetail2 = new FeeMatrixDetail();

		feeMatrixDetail1.setId(1l);
		feeMatrixDetail1.setTenantId("default");
		feeMatrixDetail1.setUomFrom(0l);
		feeMatrixDetail1.setUomTo(20l);
		feeMatrixDetail1.setAmount(150.00);

		feeMatrixDetail2.setId(2l);
		feeMatrixDetail2.setTenantId("default");
		feeMatrixDetail2.setUomFrom(20l);
		feeMatrixDetail2.setUomTo(30l);
		feeMatrixDetail2.setAmount(250.00);

		feeMatrixDetails.add(feeMatrixDetail1);
		feeMatrixDetails.add(feeMatrixDetail2);

		feeMatrix.setAuditDetails(new AuditDetails());

		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);
		feeMatrixSearch.add(feeMatrix);

		try {

			when(feeMatrixService.search(any(FeeMatrixSearchCriteria.class), any(RequestInfo.class)))
					.thenReturn(feeMatrixSearch);

			mockMvc.perform(
					post("/feematrix/v1/_search").contentType(MediaType.APPLICATION_JSON).param("tenantId", "default")
							.param("applicationType", "NEW").content(getFileContents("feeMatrixSearchRequest.json")))
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