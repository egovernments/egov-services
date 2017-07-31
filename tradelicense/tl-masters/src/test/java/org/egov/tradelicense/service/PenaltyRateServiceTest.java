package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.PenaltyRateResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.UserInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.services.PenaltyRateService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TradeLicenseApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PenaltyRateServiceTest {

	@Autowired
	PenaltyRateService penaltyRateService;

	@Autowired
	private PropertiesManager propertiesManager;

	public Long penaltyRateId = 1l;
	public String tenantId = "default";
	public String applicationType = "New";
	public Long fromRange = -9999l;
	public Long toRange = 10l;
	public Double rate = 50d;
	public Long updatedFromRange = 50l;
	public Long updatedToRange = 100l;

	@Test
	public void testAcreatePenaltyRate() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<PenaltyRate> penaltyRates = new ArrayList<>();

		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setTenantId(tenantId);
		penaltyRate.setApplicationType(ApplicationTypeEnum.fromValue(applicationType));
		penaltyRate.setFromRange(fromRange);
		penaltyRate.setToRange(toRange);
		penaltyRate.setRate(rate);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		penaltyRate.setAuditDetails(auditDetails);
		penaltyRates.add(penaltyRate);

		PenaltyRateRequest penaltyRateRequest = new PenaltyRateRequest();
		penaltyRateRequest.setPenaltyRates(penaltyRates);
		penaltyRateRequest.setRequestInfo(requestInfo);

		try {
			PenaltyRateResponse penaltyRateResponse = penaltyRateService.createPenaltyRateMaster(tenantId,
					penaltyRateRequest);
			if (penaltyRateResponse.getPenaltyRates().size() == 0) {
				assertTrue(false);
			}
			this.penaltyRateId = penaltyRateResponse.getPenaltyRates().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testAsearchPenaltyRate() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			PenaltyRateResponse penaltyRateResponse = penaltyRateService.getPenaltyRateMaster(requestInfo, tenantId,
					new Integer[] { penaltyRateId.intValue() }, applicationType, pageSize, offset);
			if (penaltyRateResponse.getPenaltyRates().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testBmodifyPenaltyRate() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<PenaltyRate> penaltyRates = new ArrayList<>();

		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setId(penaltyRateId);
		penaltyRate.setTenantId(tenantId);
		penaltyRate.setApplicationType(ApplicationTypeEnum.fromValue(applicationType));
		penaltyRate.setFromRange(updatedFromRange);
		penaltyRate.setToRange(updatedToRange);
		penaltyRate.setRate(rate);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		penaltyRate.setAuditDetails(auditDetails);
		penaltyRates.add(penaltyRate);

		PenaltyRateRequest penaltyRateRequest = new PenaltyRateRequest();
		penaltyRateRequest.setPenaltyRates(penaltyRates);
		penaltyRateRequest.setRequestInfo(requestInfo);

		try {
			PenaltyRateResponse penaltyRateResponse = penaltyRateService.updatePenaltyRateMaster(penaltyRateRequest);

			if (penaltyRateResponse.getPenaltyRates().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
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
		UserInfo userInfo = new UserInfo();
		String username = "pavan";
		userInfo.setUsername(username);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}
}