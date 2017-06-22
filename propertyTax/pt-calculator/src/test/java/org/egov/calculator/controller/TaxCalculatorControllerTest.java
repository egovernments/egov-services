package org.egov.calculator.controller;

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

import org.egov.calculator.PtCalculatorApplication;
import org.egov.calculator.api.TaxCalculatorController;
import org.egov.calculator.service.TaxCalculatorService;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
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
@WebMvcTest(TaxCalculatorController.class)
@ContextConfiguration(classes = {PtCalculatorApplication.class})
public class TaxCalculatorControllerTest {

	@MockBean
	private TaxCalculatorService taxCalculatorService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testShouldCreateFactor() {

		List<CalculationFactor> calculationFactors = new ArrayList<>();
		CalculationFactor calculationFactor = new CalculationFactor();
		calculationFactor.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		calculationFactor.setAuditDetails(auditDetails);

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();
		calculationFactors.add(calculationFactor);

		calculationFactorResponse.setResponseInfo(new ResponseInfo());
		calculationFactorResponse.setCalculationFactors(calculationFactors);

		try {
			when(taxCalculatorService.createFactor(any(String.class),
					any(CalculationFactorRequest.class)))
							.thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/factor/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(
							MediaType.APPLICATION_JSON))
					.andExpect(content().json(
							getFileContents("createFactorResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testShouldUpdateFactor() {

		List<CalculationFactor> calculationFactors = new ArrayList<>();
		CalculationFactor calculationFactor = new CalculationFactor();
		calculationFactor.setId(1l);
		calculationFactor.setTenantId("default");
		calculationFactor.setFactorCode("propertytax");
		calculationFactor.setFactorType("occupancy");
		calculationFactor.setFactorValue(1234.12);
		calculationFactor.setFromDate("10/06/2007  00:00:00");
		calculationFactor.setToDate("25/06/2017  00:00:00");

		AuditDetails auditDetails = new AuditDetails();
		calculationFactor.setAuditDetails(auditDetails);

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();
		calculationFactors.add(calculationFactor);

		calculationFactorResponse.setResponseInfo(new ResponseInfo());
		calculationFactorResponse.setCalculationFactors(calculationFactors);

		try {

			when(taxCalculatorService.updateFactor(any(String.class),
					any(CalculationFactorRequest.class)))
							.thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/factor/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(
							MediaType.APPLICATION_JSON))
					.andExpect(content().json(
							getFileContents("updateFactorResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);

		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testShouldSearchFactor() {

		List<CalculationFactor> calculationFactors = new ArrayList<>();
		CalculationFactor calculationFactor = new CalculationFactor();
		calculationFactor.setTenantId("default");
		calculationFactor.setFactorCode("propertytax");
		calculationFactor.setFactorType("occupancy");
		calculationFactor.setFactorValue(1234.12);
		calculationFactor.setFromDate("10/06/2007  00:00:00");
		calculationFactor.setToDate("25/06/2017  00:00:00");

		AuditDetails auditDetails = new AuditDetails();
		calculationFactor.setAuditDetails(auditDetails);

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();
		calculationFactors.add(calculationFactor);

		calculationFactorResponse.setResponseInfo(new ResponseInfo());
		calculationFactorResponse.setCalculationFactors(calculationFactors);

		try {

			when(taxCalculatorService.getFactor(any(RequestInfo.class),
					any(String.class), any(String.class), any(String.class),
					any(String.class))).thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/factor/_search").param("tenantId", "default")
					.param("factorType", "building")
					.param("validDate", "10/06/2007")
					.param("code", "propertytax")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(
							MediaType.APPLICATION_JSON))
					.andExpect(content().json(
							getFileContents("searchFactorResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);

		}
		assertTrue(Boolean.TRUE);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(
				new File(classLoader.getResource(fileName).getFile())
						.toPath()));
	}

}