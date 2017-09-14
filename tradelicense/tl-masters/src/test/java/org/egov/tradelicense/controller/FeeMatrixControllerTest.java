/*package org.egov.tradelicense.controller;

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
import org.egov.tl.commons.web.contract.FeeMatrixContract;
import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.response.FeeMatrixResponse;
import org.egov.tl.masters.domain.model.FeeMatrix;
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
	KafkaTemplate kafkaTemplate;

	*//**
	 * Description : Test method for createFeeMatrix() method
	 *//*
	@Test
	public void testCreateFeeMatrix() throws Exception {

		List<FeeMatrixContract> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetailContract> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetailContract feeMatrixDetail = new FeeMatrixDetailContract();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrixContract feeMatrix = new FeeMatrixContract();
		feeMatrix.setTenantId("default");
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		AuditDetails auditDetails = new AuditDetails();
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrices.add(feeMatrix);

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		feeMatrixResponse.setResponseInfo(new ResponseInfo());
		feeMatrixResponse.setFeeMatrices(feeMatrices);

		try {

			when(feeMatrixService.createFeeMatrixMaster(any(ArrayList.class,RequestInfo.class))).thenReturn(feeMatrixResponse);

			mockMvc.perform(post("/feematrix/v1/_create").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("feeMatrixCreateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	*//**
	 * Description : Test method for updateFeeMatrix() method
	 *//*
	@Test
	public void testUpdateFeeMatrix() throws Exception {

		List<FeeMatrixContract> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetailContract> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetailContract feeMatrixDetail = new FeeMatrixDetailContract();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrixContract feeMatrix = new FeeMatrixContract();
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

			mockMvc.perform(post("/feematrix/v1/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("feeMatrixUpdateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("feeMatrixUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	*//**
	 * Description : Test method for searchFeeMatrix() method
	 *//*
	@Test
	public void testSearchFeeMatrix() throws Exception {

		List<FeeMatrixContract> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetailContract> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetailContract feeMatrixDetail = new FeeMatrixDetailContract();
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrixContract feeMatrix = new FeeMatrixContract();
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

			mockMvc.perform(post("/feematrix/v1/_search").param("tenantId", "default")
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
}*/