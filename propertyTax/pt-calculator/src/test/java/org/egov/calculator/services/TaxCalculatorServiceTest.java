package org.egov.calculator.services;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.PtCalculatorApplication;
import org.egov.calculator.service.TaxCalculatorService;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.junit.FixMethodOrder;
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
@ContextConfiguration(classes = {PtCalculatorApplication.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaxCalculatorServiceTest {

	@Autowired
	TaxCalculatorService taxCalculatorService;

	@Autowired
	Environment environment;

	public Long factorId = 1l;

	@Test
	public void createFactor() {
		String tenantId = "default";
		RequestInfo requestInfo = getRequestInfoObject();

		List<CalculationFactor> calculationFactors = new ArrayList<>();

		CalculationFactor calculationFactor = new CalculationFactor();
		calculationFactor.setTenantId("default");
		calculationFactor.setFactorCode("propertytax");
		calculationFactor.setFactorType("occupancy");
		calculationFactor.setFactorValue(1234.12);
		calculationFactor.setFromDate("10/06/2007  00:00:00");
		calculationFactor.setToDate("25/06/2017  00:00:00");
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
			CalculationFactorResponse calculationFactorResponse = taxCalculatorService
					.createFactor(tenantId, calculationFactorRequest);
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
		calculationFactor.setFactorType("usage");
		calculationFactor.setFactorValue(1234.12);
		calculationFactor.setFromDate("10/06/2007  00:00:00");
		calculationFactor.setToDate("25/06/2017  00:00:00");
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
			CalculationFactorResponse calculationFactorResponse = taxCalculatorService
					.updateFactor(tenantId, calculationFactorRequest);

			if (calculationFactorResponse.getCalculationFactors().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchFactor() {

		String tenantId = "default";
		String factorType = "usage";
		String validDate = "16/06/2007";
		String code = "propertytax";
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CalculationFactorResponse calculationFactorResponse = taxCalculatorService
					.getFactor(requestInfo, tenantId, factorType, validDate,
							code);
			if (calculationFactorResponse.getCalculationFactors().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

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

		return requestInfo;
	}
}
