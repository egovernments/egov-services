package org.egov.calculator.services;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.PtCalculatorApplication;
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
import org.egov.models.GuidanceValueSearchCriteria;
import org.egov.models.RequestInfo;
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
import org.egov.models.UserInfo;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { PtCalculatorApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaxCalculatorMasterServiceTest {

    @Autowired
    TaxCalculatorMasterService taxCalculatorMasterService;

    @Autowired
    Environment environment;

    public Long factorId = 1l;

    public Long taxRatesId = 1l;

    @Test
    public void createFactor() {
        String tenantId = "default";
        RequestInfo requestInfo = getRequestInfoObject();

        List<CalculationFactor> calculationFactors = new ArrayList<>();

        CalculationFactor calculationFactor = new CalculationFactor();
        calculationFactor.setTenantId("default");
        calculationFactor.setFactorCode("propertytax");
        calculationFactor.setFactorType(CalculationFactorTypeEnum.AGE);
        calculationFactor.setFactorValue(1234.12);
        calculationFactor.setFromDate("10/06/2007 00:00:00");
        calculationFactor.setToDate("25/06/2017 00:00:00");
        long createdTime = new Date().getTime();

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("pavan");
        auditDetails.setLastModifiedBy("pavan");
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedTime(createdTime);

        calculationFactor.setAuditDetails(auditDetails);
        calculationFactors.add(calculationFactor);

        CalculationFactorRequest calculationFactorRequest = new CalculationFactorRequest();
        calculationFactorRequest.setCalculationFactors(calculationFactors);
        calculationFactorRequest.setRequestInfo(requestInfo);

        try {
            CalculationFactorResponse calculationFactorResponse = taxCalculatorMasterService.createFactor(tenantId,
                    calculationFactorRequest);
            if (calculationFactorResponse.getCalculationFactors().size() == 0)
                assertTrue(false);

            assertTrue(true);

        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
    public void modifyFactor() {
        String tenantId = "default";
        RequestInfo requestInfo = getRequestInfoObject();
        List<CalculationFactor> calculationFactors = new ArrayList<>();

        CalculationFactor calculationFactor = new CalculationFactor();
        calculationFactor.setTenantId("default");
        calculationFactor.setFactorCode("propertytax");
        calculationFactor.setFactorType(CalculationFactorTypeEnum.AGE);
        calculationFactor.setFactorValue(1234.12);
        calculationFactor.setFromDate("10/06/2007 00:00:00");
        calculationFactor.setToDate("25/06/2017 00:00:00");
        calculationFactor.setId(factorId);
        long createdTime = new Date().getTime();

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("pavan");
        auditDetails.setLastModifiedBy("pavan");
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedTime(createdTime);

        calculationFactor.setAuditDetails(auditDetails);
        calculationFactors.add(calculationFactor);

        CalculationFactorRequest calculationFactorRequest = new CalculationFactorRequest();
        calculationFactorRequest.setCalculationFactors(calculationFactors);
        calculationFactorRequest.setRequestInfo(requestInfo);

        try {
            CalculationFactorResponse calculationFactorResponse = taxCalculatorMasterService.updateFactor(tenantId,
                    calculationFactorRequest);

            if (calculationFactorResponse.getCalculationFactors().size() > 0 && calculationFactorRequest
                    .getCalculationFactors().equals(calculationFactorResponse.getCalculationFactors()))
                assertTrue(true);
            else
                assertTrue(false);

        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
	public void searchFactor() {

		CalculationFactorSearchCriteria calculationFactorSearchCriteria = new CalculationFactorSearchCriteria();
		calculationFactorSearchCriteria.setTenantId("default");
		calculationFactorSearchCriteria.setFactorType("age");
		calculationFactorSearchCriteria.setValidDate("16/06/2007");
		calculationFactorSearchCriteria.setCode("propertytax");
		RequestInfo requestInfo = getRequestInfoObject();

		try {
			CalculationFactorResponse calculationFactorResponse = taxCalculatorMasterService.getFactor(requestInfo,
					calculationFactorSearchCriteria);
			if (calculationFactorResponse.getCalculationFactors().size() == 0) {
				assertTrue(false);
			}

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

    /**
     * Description: test case for create guidance value
     * 
     * @throws Exception
     */
    @Test
    public void createGuidanceValue() throws Exception {
        try {

            String tenantId = "default";

            List<GuidanceValue> guidanceValue = new ArrayList<GuidanceValue>();

            GuidanceValue master = new GuidanceValue();
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedBy("anil");
            auditDetails.setCreatedTime((long) 123456);
            auditDetails.setLastModifiedBy("anil");
            auditDetails.setLastModifiedTime((long) 123456);

            master.setTenantId("default");
            master.setName("anil");
            master.setBoundary("b1");
            master.setStructure("rectangle");
            master.setUsage("propertyuse");
            master.setSubUsage("propertyusage");
            master.setOccupancy("anil");
            master.setValue((double) 252);
            master.setFromDate("25/10/2016 00:00:00");
            master.setToDate("25/10/2017 00:00:00");

            master.setAuditDetails(auditDetails);
            guidanceValue.add(master);

            GuidanceValueRequest guidanceValueRequest = new GuidanceValueRequest();
            guidanceValueRequest.setRequestInfo(getRequestInfoObject());
            guidanceValueRequest.setGuidanceValues(guidanceValue);

            GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.createGuidanceValue(tenantId,
                    guidanceValueRequest);
            if (guidanceValueResponse.getGuidanceValues().size() == 0) {
                assertTrue(false);
            }
            assertTrue(true);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * Description: Test case for update guidance value
     * 
     * @throws Exception
     */
    @Test
    public void modityGuidanceValue() throws Exception {
        try {

            String tenantId = "default";

            List<GuidanceValue> guidanceValue = new ArrayList<GuidanceValue>();

            GuidanceValue master = new GuidanceValue();
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedBy("anil");
            auditDetails.setCreatedTime((long) 123456);
            auditDetails.setLastModifiedBy("anil");
            auditDetails.setLastModifiedTime((long) 123456);
            master.setId((long) 1);
            master.setTenantId("default");
            master.setName("kumar");
            master.setBoundary("b2");
            master.setStructure("rectangle");
            master.setUsage("propertyuse");
            master.setSubUsage("propertyusage");
            master.setOccupancy("anil");
            master.setValue((double) 252);
            master.setFromDate("25/10/2016 00:00:00");
            master.setToDate("25/10/2017 00:00:00");
            master.setAuditDetails(auditDetails);
            guidanceValue.add(master);

            GuidanceValueRequest guidanceValueRequest = new GuidanceValueRequest();
            guidanceValueRequest.setRequestInfo(getRequestInfoObject());
            guidanceValueRequest.setGuidanceValues(guidanceValue);

            GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.updateGuidanceValue(tenantId,
                    guidanceValueRequest);
            if (guidanceValueResponse.getGuidanceValues().size() > 0
                    && guidanceValueRequest.getGuidanceValues().equals(guidanceValueResponse.getGuidanceValues()))
                assertTrue(true);
            else
                assertTrue(false);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * Description: Test case for guidance value search
     * 
     * @throws Exception
     */
    @Test
    public void searchGuidanceValue() throws Exception {
        try {
        	GuidanceValueSearchCriteria guidanceValueSearchCriteria = new GuidanceValueSearchCriteria();
        	guidanceValueSearchCriteria.setTenantId("default");
        	guidanceValueSearchCriteria.setBoundary("b2");
        	guidanceValueSearchCriteria.setValidDate("25-11-2016");
        	guidanceValueSearchCriteria.setStructure("rectangle");
        	guidanceValueSearchCriteria.setUsage("propertyuse");
        	guidanceValueSearchCriteria.setSubUsage("propertyusage");
        	guidanceValueSearchCriteria.setOccupancy("anil");
            GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.getGuidanceValue(
                    getRequestInfoObject(), guidanceValueSearchCriteria);
            if (guidanceValueResponse.getGuidanceValues().size() == 0) {
                assertTrue(false);
            }
            assertTrue(true);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * This will test whether the tax period will be created successfully or not
     */
    @Test
    public void createTaxPeriod() {
        List<TaxPeriod> taxPeriods = new ArrayList<>();
        TaxPeriod taxPeriod = new TaxPeriod();

        String tenantId = "1234";
        taxPeriod.setTenantId("1234");
        taxPeriod.setCode("MON");
        taxPeriod.setFinancialYear("2017-18");
        taxPeriod.setFromDate("02/02/2017 00:00:00");
        taxPeriod.setToDate("05/02/2017 00:00:00");
        taxPeriod.setPeriodType("Yearly");

        long createdTime = new Date().getTime();
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("prasad");
        auditDetails.setLastModifiedBy("prasad");
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedTime(createdTime);

        taxPeriod.setAuditDetails(auditDetails);

        taxPeriods.add(taxPeriod);
        TaxPeriodRequest taxPeriodRequest = new TaxPeriodRequest();
        taxPeriodRequest.setRequestInfo(getRequestInfoObject());
        taxPeriodRequest.setTaxPeriods(taxPeriods);

        TaxPeriodResponse taxPeriodResponse = null;
        try {
            taxPeriodResponse = taxCalculatorMasterService.createTaxPeriod(tenantId, taxPeriodRequest);
        } catch (Exception e) {
            assertTrue(false);
        }
        if (taxPeriodResponse.getTaxPeriods().size() == 0)
            assertTrue(false);

        assertTrue(true);

    }

    /**
     * This will test whether the tax period will update successfully or not
     */
    @Test
    public void modifyTaxPeriod() {

        List<TaxPeriod> taxPeriods = new ArrayList<>();
        TaxPeriod taxPeriod = new TaxPeriod();

        String tenantId = "1234";
        taxPeriod.setTenantId("1234");
        taxPeriod.setCode("YEAR");
        taxPeriod.setFinancialYear("2019-20");
        taxPeriod.setFromDate("02/02/2017 00:00:00");
        taxPeriod.setToDate("05/02/2017 00:00:00");
        taxPeriod.setPeriodType("Yearly");

        long createdTime = new Date().getTime();
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("pankaj");
        auditDetails.setLastModifiedBy("pankaj");
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedTime(createdTime);

        taxPeriod.setAuditDetails(auditDetails);

        taxPeriods.add(taxPeriod);
        TaxPeriodRequest taxPeriodRequest = new TaxPeriodRequest();
        taxPeriodRequest.setRequestInfo(getRequestInfoObject());
        taxPeriodRequest.setTaxPeriods(taxPeriods);

        TaxPeriodResponse taxPeriodResponse = null;
        try {
            taxPeriodResponse = taxCalculatorMasterService.updateTaxPeriod(tenantId, taxPeriodRequest);

            if (taxPeriodResponse.getTaxPeriods().size() > 0
                    && taxPeriodResponse.getTaxPeriods().equals(taxPeriodRequest.getTaxPeriods()))
                assertTrue(true);
            else
                assertTrue(false);

        } catch (Exception e) {
            assertTrue(false);
        }

    }

    /**
     * This will test whether the search will be successful or not for tax periods
     */
	@Ignore
	public void searchTaxPeriod() {
		TaxPeriodSearchCriteria taxPeriodSearchCriteria = new TaxPeriodSearchCriteria();
		taxPeriodSearchCriteria.setTenantId("1234");
		taxPeriodSearchCriteria.setCode("YEAR");
		taxPeriodSearchCriteria.setFromDate("02/02/2017");
		taxPeriodSearchCriteria.setToDate("05-02-2017");
		TaxPeriodResponse taxPeriodResponse = null;
		try {
			taxPeriodResponse = taxCalculatorMasterService.getTaxPeriod(getRequestInfoObject(),
					taxPeriodSearchCriteria);

			if (taxPeriodResponse.getTaxPeriods().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

    @Test
    public void createTaxRateServiceTest() {

        String tenantId = "default";
        RequestInfo requestInfo = getRequestInfoObject();
        List<TaxRates> listOfTaxRates = new ArrayList<>();

        TaxRates taxRates = new TaxRates();
        taxRates.setTenantId("default");
        taxRates.setTaxHead("taxHead-C");
        taxRates.setDependentTaxHead("dependentTaxHead-C");
        taxRates.setFromDate("03/06/2017 00:00:00");
        taxRates.setToDate("10/06/2017 00:00:00");
        taxRates.setFromValue(1000.0);
        taxRates.setToValue(1500.0);
        taxRates.setRatePercentage(33.0);
        taxRates.setTaxFlatValue(2222.0);
        taxRates.setUsage("car usage");
        taxRates.setPropertyType("vaccant land");

        long createdTime = new Date().getTime();

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("yosadhara");
        auditDetails.setLastModifiedBy("yosadhara");
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedTime(createdTime);

        taxRates.setAuditDetails(auditDetails);
        listOfTaxRates.add(taxRates);

        TaxRatesRequest taxRatesRequest = new TaxRatesRequest();
        taxRatesRequest.setTaxRates(listOfTaxRates);
        taxRatesRequest.setRequestInfo(requestInfo);

        try {
            TaxRatesResponse taxRatesResponse = taxCalculatorMasterService.createTaxRate(tenantId, taxRatesRequest);

            if (taxRatesResponse.getTaxRates().size() == 0)
                assertTrue(false);

            assertTrue(true);
        } catch (Exception e) {

            assertTrue(false);
        }
    }

    @Test
    public void modifyTaxRateServiceTest() {

        String tenantId = "default";
        RequestInfo requestInfo = getRequestInfoObject();
        List<TaxRates> listOfTaxRates = new ArrayList<>();

        TaxRates taxRates = new TaxRates();
        taxRates.setTenantId("default");
        taxRates.setTaxHead("taxHead-UU");
        taxRates.setDependentTaxHead("dependentTaxHead-UU");
        taxRates.setFromDate("03/06/2017 00:00:00");
        taxRates.setToDate("10/06/2017 00:00:00");
        taxRates.setFromValue(1000.0);
        taxRates.setToValue(1500.0);
        taxRates.setRatePercentage(33.0);
        taxRates.setTaxFlatValue(2222.0);
        taxRates.setUsage("car usage");
        taxRates.setPropertyType("vaccant land");
        taxRates.setId(taxRatesId);

        long updatedTime = new Date().getTime();

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("yosadhara");
        auditDetails.setLastModifiedBy("yosadhara");
        auditDetails.setCreatedTime(updatedTime);
        auditDetails.setLastModifiedTime(updatedTime);

        taxRates.setAuditDetails(auditDetails);
        listOfTaxRates.add(taxRates);

        TaxRatesRequest taxRatesRequest = new TaxRatesRequest();
        taxRatesRequest.setTaxRates(listOfTaxRates);
        taxRatesRequest.setRequestInfo(requestInfo);

        try {
            TaxRatesResponse taxRatesResponse = taxCalculatorMasterService.updateTaxRate(tenantId, taxRatesRequest);

            if (taxRatesResponse.getTaxRates().size() > 0
                    && taxRatesRequest.getTaxRates().equals(taxRatesResponse.getTaxRates()))
                assertTrue(true);
            else
                assertTrue(false);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

	@Test
	public void searchTaxRateServiceTest() {

		TaxRatesSearchCriteria taxRatesSearchCriteria = new TaxRatesSearchCriteria();
		taxRatesSearchCriteria.setTenantId("default");
		taxRatesSearchCriteria.setTaxHead("taxHead-UU");
		taxRatesSearchCriteria.setValidDate("04-06-2017");
		taxRatesSearchCriteria.setValidARVAmount(1100.0);
		taxRatesSearchCriteria.setParentTaxHead("dependentTaxHead-UU");
		taxRatesSearchCriteria.setUsage("car usage");
		taxRatesSearchCriteria.setPropertyType("vaccant land");
		TaxRatesResponse taxRatesResponse = null;
		try {
			taxRatesResponse = taxCalculatorMasterService.getTaxRate(getRequestInfoObject(), taxRatesSearchCriteria);

			if (taxRatesResponse.getTaxRates().size() == 0) {
				assertTrue(false);
			}

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

    /**
	 * This test will test whether the TransferFeeRate will be created successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldCreateTransferFeeRate() throws Exception {
		String tenantId = "default";
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		TransferFeeRate transferFeeRate = new TransferFeeRate();
		transferFeeRate.setTenantId("default");
		transferFeeRate.setFeeFactor(TransferFeeRatesEnum.fromValue("FLATRATE"));
		transferFeeRate.setFromDate("19/10/2017");
		transferFeeRate.setFromValue((double) 7000);
		transferFeeRate.setToValue((double) 8000);
		transferFeeRate.setFeePercentage(20d);
		transferFeeRate.setFlatValue(1600d);
		transferFeeRates.add(transferFeeRate);
		RequestInfo requestInfo = getRequestInfoObject();
		TransferFeeRatesRequest transferFeeRatesRequest = new TransferFeeRatesRequest();
		transferFeeRatesRequest.setRequestInfo(requestInfo);
		transferFeeRatesRequest.setTransferFeeRates(transferFeeRates);
		try {
			TransferFeeRatesResponse transferFeeRatesResponse = taxCalculatorMasterService
					.createTransferFeeRate(transferFeeRatesRequest, tenantId);
			if (transferFeeRatesResponse.getTransferFeeRates().size() > 0 && transferFeeRatesRequest
					.getTransferFeeRates().equals(transferFeeRatesResponse.getTransferFeeRates()))
				assertTrue(true);
			else 
				assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	/**
	 * This test will test whether the TransferFeeRate will be updated successfully or
	 * not
	 * 
	 */
	@Test
	public void testShouldModifyTransferFeeRate() throws Exception {
		String tenantId = "default1";
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		TransferFeeRate transferFeeRate = new TransferFeeRate();
		transferFeeRate.setId(1l);
		transferFeeRate.setTenantId("default1");
		transferFeeRate.setFeeFactor(TransferFeeRatesEnum.fromValue("MARKETVALUE"));
		transferFeeRate.setFromDate("19/10/2017");
		transferFeeRate.setToDate("29/10/2017");
		transferFeeRate.setFromValue((double) 7777);
		transferFeeRate.setToValue((double) 8888);
		transferFeeRate.setFeePercentage(30d);
		transferFeeRate.setFlatValue(1100d);
		transferFeeRates.add(transferFeeRate);
		RequestInfo requestInfo = getRequestInfoObject();
		TransferFeeRatesRequest transferFeeRatesRequest = new TransferFeeRatesRequest();
		transferFeeRatesRequest.setRequestInfo(requestInfo);
		transferFeeRatesRequest.setTransferFeeRates(transferFeeRates);
		try {
			TransferFeeRatesResponse transferFeeRatesResponse = taxCalculatorMasterService.updateTransferFeeRate(transferFeeRatesRequest, tenantId);
			if (transferFeeRatesResponse.getTransferFeeRates().size() > 0 && transferFeeRatesRequest
					.getTransferFeeRates().equals(transferFeeRatesResponse.getTransferFeeRates()))
				assertTrue(true);
			else 
				assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	/**
	 * This test will test whether the TransferFeeRate search searching
	 * 
	 */
	@Test
	public void testShouldSearchTransferFeeRate() throws Exception {
		
		TransferFeeRateSearchCriteria transferFeeRateSearchCriteria = new TransferFeeRateSearchCriteria();
		transferFeeRateSearchCriteria.setTenantId("default1");
		transferFeeRateSearchCriteria.setFeeFactor("MARKETVALUE");
		transferFeeRateSearchCriteria.setValidDate("19/10/2017");
		transferFeeRateSearchCriteria.setValidValue(7777d);		
		RequestInfo requestInfo = getRequestInfoObject();
		
		try {
			TransferFeeRatesResponse transferFeeRatesResponse = taxCalculatorMasterService
					.getTransferFeeRate(requestInfo, transferFeeRateSearchCriteria);
			if (transferFeeRatesResponse.getTransferFeeRates().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

    /**
     * This will return the request Info Object
     * 
     * @return {@link RequestInfo}
     */
    private RequestInfo getRequestInfoObject() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("emp");
        requestInfo.setVer("1.0");
        requestInfo.setTs(new Long(122366));
        requestInfo.setDid("1");
        requestInfo.setKey("abcdkey");
        requestInfo.setMsgId("20170310130900");
        requestInfo.setRequesterId("rajesh");
        requestInfo.setAuthToken("b5da31a4-b400-4d6e-aa46-9ebf33cce933");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(123);
        requestInfo.setUserInfo(userInfo);

        return requestInfo;
    }
}
