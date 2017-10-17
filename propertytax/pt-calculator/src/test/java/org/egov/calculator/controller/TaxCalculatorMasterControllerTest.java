package org.egov.calculator.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
import org.egov.calculator.api.TaxCalculatorMasterController;
import org.egov.calculator.service.TaxCalculatorMasterService;
import org.egov.enums.CalculationFactorTypeEnum;
import org.egov.enums.TransferFeeRatesEnum;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationFactorSearchCriteria;
import org.egov.models.GuidanceValue;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxPeriodSearchCriteria;
import org.egov.models.TaxRates;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TaxRatesSearchCriteria;
import org.egov.models.TransferFeeRate;
import org.egov.models.TransferFeeRateSearchCriteria;
import org.egov.models.TransferFeeRatesRequest;
import org.egov.models.TransferFeeRatesResponse;
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
@WebMvcTest(TaxCalculatorMasterController.class)
@ContextConfiguration(classes = { PtCalculatorApplication.class })
public class TaxCalculatorMasterControllerTest {

	@MockBean
	private TaxCalculatorMasterService taxCalculatorMasterService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private KafkaTemplate kafkaTemplate;

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
			when(taxCalculatorMasterService.createFactor(any(String.class), any(CalculationFactorRequest.class)))
					.thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/properties/taxes/factor/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createFactorResponse.json")));

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
		calculationFactor.setFactorType(CalculationFactorTypeEnum.OCCUPANCY);
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

			when(taxCalculatorMasterService.updateFactor(any(String.class), any(CalculationFactorRequest.class)))
					.thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/properties/taxes/factor/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateFactorResponse.json")));

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
		calculationFactor.setFactorType(CalculationFactorTypeEnum.OCCUPANCY);
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

			when(taxCalculatorMasterService.getFactor(any(RequestInfo.class), any(CalculationFactorSearchCriteria.class))).thenReturn(calculationFactorResponse);

			mockMvc.perform(post("/properties/taxes/factor/_search").param("tenantId", "default")
					.param("factorType", "building").param("validDate", "10/06/2007").param("code", "propertytax")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchFactorRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchFactorResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);

		}
		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: Test case for create guidance value using rest controller
	 */
	@Test
	public void testShouldCreateGuidanceValue() {
		List<GuidanceValue> guidanceValues = new ArrayList<>();
		GuidanceValue guidanceValue = new GuidanceValue();
		guidanceValue.setTenantId("default");
		guidanceValue.setName("anil");
		guidanceValue.setBoundary("b1");

		AuditDetails auditDetails = new AuditDetails();
		guidanceValue.setAuditDetails(auditDetails);

		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();
		guidanceValues.add(guidanceValue);

		guidanceValueResponse.setResponseInfo(new ResponseInfo());
		guidanceValueResponse.setGuidanceValues(guidanceValues);

		try {
			when(taxCalculatorMasterService.createGuidanceValue(any(String.class), any(GuidanceValueRequest.class)))
					.thenReturn(guidanceValueResponse);
			mockMvc.perform(post("/properties/taxes/guidancevalue/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createguidancevaluerequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createguidancevalueresponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: Test case for update guidance value using rest controller
	 */
	@Test
	public void testShouldUpdateGuidanceValue() {

		List<GuidanceValue> guidanceValues = new ArrayList<>();
		GuidanceValue guidanceValue = new GuidanceValue();
		guidanceValue.setTenantId("default");
		guidanceValue.setName("kumar");
		guidanceValue.setBoundary("b2");

		AuditDetails auditDetails = new AuditDetails();
		guidanceValue.setAuditDetails(auditDetails);

		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();
		guidanceValues.add(guidanceValue);

		guidanceValueResponse.setResponseInfo(new ResponseInfo());
		guidanceValueResponse.setGuidanceValues(guidanceValues);

		try {
			when(taxCalculatorMasterService.updateGuidanceValue(any(String.class), any(GuidanceValueRequest.class)))
					.thenReturn(guidanceValueResponse);

			mockMvc.perform(post("/properties/taxes/guidancevalue/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateguidancevaluerequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateguidancevalueresponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	/**
	 * Description: Test case for search guidance value using rest controller
	 */
	/*
	 * @Test public void testShouldSearchGuidanceValue() {
	 * 
	 * GuidanceValueResponse guidanceValueResponse = new
	 * GuidanceValueResponse(); List<GuidanceValue> guidanceValues = new
	 * ArrayList<>(); GuidanceValue guidanceValue = new GuidanceValue();
	 * guidanceValue.setTenantId("default"); guidanceValue.setName("kumar");
	 * guidanceValue.setBoundary("b2");
	 * 
	 * AuditDetails auditDetails = new AuditDetails();
	 * guidanceValue.setAuditDetails(auditDetails);
	 * 
	 * guidanceValues.add(guidanceValue);
	 * 
	 * guidanceValueResponse.setResponseInfo(new ResponseInfo());
	 * guidanceValueResponse.setGuidanceValues(guidanceValues);
	 * 
	 * try {
	 * 
	 * when(taxCalculatorMasterService.getGuidanceValue(any(RequestInfo.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class))).thenReturn(guidanceValueResponse);
	 * 
	 * mockMvc.perform(post("/properties/taxes/guidancevalue/_search").param(
	 * "tenantId", "default") .param("boundary", "b2").param("validDate",
	 * "15/06/2017").contentType(MediaType.APPLICATION_JSON)
	 * .content(getFileContents("searchguidancevaluerequest.json"))).andExpect(
	 * status().isOk())
	 * .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON
	 * )) .andExpect(content().json(getFileContents(
	 * "searchguidancevalueresponse.json")));
	 * 
	 * } catch (Exception e) { assertTrue(Boolean.FALSE); }
	 * 
	 * assertTrue(Boolean.TRUE); }
	 */

	/**
	 * This test will test whether the tax period is created successfully or not
	 */
	@Test
	public void testShouldCreateTaxPeriod() {

		List<TaxPeriod> taxPeriods = new ArrayList<>();
		TaxPeriod taxPeriod = new TaxPeriod();
		taxPeriod.setTenantId("1234");
		taxPeriod.setCode("MON");
		AuditDetails auditDetails = new AuditDetails();
		taxPeriod.setAuditDetails(auditDetails);

		taxPeriods.add(taxPeriod);
		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		taxPeriodResponse.setResponseInfo(new ResponseInfo());
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		try {
			when(taxCalculatorMasterService.createTaxPeriod(anyString(), any(TaxPeriodRequest.class)))
					.thenReturn(taxPeriodResponse);

			mockMvc.perform(post("/properties/taxes/taxperiods/_create").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createTaxPeriodRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createTaxPeriodResponse.json")));
		} catch (Exception e) {
			assertTrue(false);

		}

		assertTrue(true);

	}

	/**
	 * This will test whether the tax period object is updated successfully or
	 * not
	 */
	@Test
	public void testshouldUpdateTaxPeriod() {

		List<TaxPeriod> taxPeriods = new ArrayList<>();
		TaxPeriod taxPeriod = new TaxPeriod();
		taxPeriod.setTenantId("1234");
		taxPeriod.setCode("YEAR");
		AuditDetails auditDetails = new AuditDetails();
		taxPeriod.setAuditDetails(auditDetails);

		taxPeriods.add(taxPeriod);
		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		taxPeriodResponse.setResponseInfo(new ResponseInfo());
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		try {
			when(taxCalculatorMasterService.updateTaxPeriod(anyString(), any(TaxPeriodRequest.class)))
					.thenReturn(taxPeriodResponse);

			mockMvc.perform(post("/properties/taxes/taxperiods/_update").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateTaxPeriodRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateTaxPeriodResponse.json")));
		} catch (Exception e) {
			assertTrue(false);

		}

		assertTrue(true);
	}

	/**
	 * This will test whether the working of tax period's searching
	 */
	@Test
	public void testShouldSearchTaxPeriod() {

		List<TaxPeriod> taxPeriods = new ArrayList<>();
		TaxPeriod taxPeriod = new TaxPeriod();
		taxPeriod.setTenantId("1234");
		taxPeriod.setCode("ganesha");
		taxPeriod.setPeriodType("MONTH");
		taxPeriod.setFinancialYear("2017-18");
		taxPeriod.setFromDate("02/02/2017");
		taxPeriod.setToDate("02/02/2017");
		AuditDetails auditDetails = new AuditDetails();
		taxPeriod.setAuditDetails(auditDetails);

		taxPeriods.add(taxPeriod);
		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		taxPeriodResponse.setResponseInfo(new ResponseInfo());
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		try {
			when(taxCalculatorMasterService.getTaxPeriod(any(RequestInfo.class), any(TaxPeriodSearchCriteria.class)))
					.thenReturn(taxPeriodResponse);

			mockMvc.perform(post("/properties/taxes/taxperiods/_search").param("tenantId", "1234")
					.param("validDate", "02/02/2017").param("code", "ganesha").param("fromDate", "01/02/2017")
					.param("toDate", "01/03/2017").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchTaxPeriodRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchTaxPeriodResponse.json")));
		} catch (Exception e) {
			assertTrue(false);

		}
	}

	/**
	 * This test will test whether the tax rate will be created successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldCreateTaxRates() throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		TaxRates taxRates = new TaxRates();
		taxRates.setTenantId("default");
		List<TaxRates> listOfTaxRates = new ArrayList<>();
		AuditDetails auditDetails = new AuditDetails();
		taxRates.setAuditDetails(auditDetails);
		listOfTaxRates.add(taxRates);
		taxRatesResponse.setResponseInfo(new ResponseInfo());
		taxRatesResponse.setTaxRates(listOfTaxRates);

		try {

			when(taxCalculatorMasterService.createTaxRate(any(String.class), any(TaxRatesRequest.class)))
					.thenReturn(taxRatesResponse);

			mockMvc.perform(post("/properties/taxes/taxrates/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("taxratesCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("taxratesCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}

	/**
	 * This test will test whether the tax rate will be updated successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldUpdateTaxRates() throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		TaxRates taxRates = new TaxRates();
		List<TaxRates> listOfTaxRates = new ArrayList<>();
		taxRates.setTenantId("default");
		AuditDetails auditDetails = new AuditDetails();
		taxRates.setAuditDetails(auditDetails);
		listOfTaxRates.add(taxRates);
		taxRatesResponse.setResponseInfo(new ResponseInfo());
		taxRatesResponse.setTaxRates(listOfTaxRates);

		try {

			when(taxCalculatorMasterService.updateTaxRate(any(String.class), any(TaxRatesRequest.class)))
					.thenReturn(taxRatesResponse);
			mockMvc.perform(post("/properties/taxes/taxrates/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("taxratesUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("taxratesUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}

	/**
	 * This test will test whether the tax rate search searching
	 * 
	 */
	@Test
	public void testShouldSearchTaxRates() throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		TaxRates taxRates = new TaxRates();
		taxRates.setTenantId("default");
		List<TaxRates> listOfTaxRates = new ArrayList<>();
		listOfTaxRates.add(taxRates);
		taxRatesResponse.setResponseInfo(new ResponseInfo());
		taxRatesResponse.setTaxRates(listOfTaxRates);

		try {

			when(taxCalculatorMasterService.getTaxRate(any(RequestInfo.class), any(TaxRatesSearchCriteria.class)))
							.thenReturn(taxRatesResponse);

			mockMvc.perform(post("/properties/taxes/taxrates/_search").param("tenantId", "default")
					.param("taxHead", "taxHead-C").param("validDate", "04/06/2017").param("validARVAmount", "1100")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("taxratesSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("taxratesSearchResponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}
	
	/**
	 * This test will test whether the TransferFeeRate will be created successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldCreateTransferFeeRate() throws Exception {
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		TransferFeeRate transferFeeRate = new TransferFeeRate();
		transferFeeRate.setTenantId("default");
		transferFeeRate.setFeeFactor(TransferFeeRatesEnum.fromValue("PROPERTYTAX"));
		transferFeeRate.setFromDate("19/10/2017");
		transferFeeRate.setFromValue((double) 7000);
		transferFeeRate.setToValue((double) 8000);
		AuditDetails auditDetails = new AuditDetails();
		transferFeeRate.setAuditDetails(auditDetails);
		transferFeeRates.add(transferFeeRate);
		transferFeeRatesResponse.setResponseInfo(new ResponseInfo());
		transferFeeRatesResponse.setTransferFeeRates(transferFeeRates);

		try {
			when(taxCalculatorMasterService.createTransferFeeRate(any(TransferFeeRatesRequest.class),
					any(String.class))).thenReturn(transferFeeRatesResponse);
			mockMvc.perform(post("/properties/taxes/transferfeerates/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createTransferFeeRateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createTransferFeeRateResponse.json")));
		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}
	
	/**
	 * This test will test whether the TransferFeeRate will be updated successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldUpdateTransferFeeRate() throws Exception {
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		TransferFeeRate transferFeeRate = new TransferFeeRate();
		transferFeeRate.setId(1l);
		transferFeeRate.setTenantId("default");
		transferFeeRate.setFeeFactor(TransferFeeRatesEnum.fromValue("PROPERTYTAX"));
		transferFeeRate.setFromDate("19/10/2017");
		transferFeeRate.setFromValue((double) 7777);
		transferFeeRate.setToValue((double) 8888);
		AuditDetails auditDetails = new AuditDetails();
		transferFeeRate.setAuditDetails(auditDetails);
		transferFeeRates.add(transferFeeRate);
		transferFeeRatesResponse.setResponseInfo(new ResponseInfo());
		transferFeeRatesResponse.setTransferFeeRates(transferFeeRates);

		try {
			when(taxCalculatorMasterService.updateTransferFeeRate(any(TransferFeeRatesRequest.class),
					any(String.class))).thenReturn(transferFeeRatesResponse);
			mockMvc.perform(post("/properties/taxes/transferfeerates/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateTransferFeeRateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateTransferFeeRateResponse.json")));
		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}
	
	/**
	 * This test will test whether the TransferFeeRate search searching
	 * 
	 */
	@Test
	public void testShouldSearchTransferFeeRate() throws Exception {
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		TransferFeeRate transferFeeRate = new TransferFeeRate();
		transferFeeRate.setTenantId("default");
		transferFeeRates.add(transferFeeRate);
		transferFeeRatesResponse.setResponseInfo(new ResponseInfo());
		transferFeeRatesResponse.setTransferFeeRates(transferFeeRates);
		try {
			when(taxCalculatorMasterService.getTransferFeeRate(any(RequestInfo.class), any(TransferFeeRateSearchCriteria.class))).thenReturn(transferFeeRatesResponse);
			mockMvc.perform(post("/properties/taxes/transferfeerates/_search").param("tenantId", "default")
					.param("feeFactor", "PROPERTYTAX").param("validDate", "17/10/2017").param("validValue", "555")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchTransferFeeRateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchTransferFeeRateResponse.json")));
		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}

	/**
	 *
	 * @param fileName
	 * @return {@link String} content of the given file
	 * @throws IOException
	 */
	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}

}