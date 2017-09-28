package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.PenaltyRate;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.requests.PenaltyRateRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.PenaltyRateResponse;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.consumers.PenaltyRateConsumer;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.services.PenaltyRateService;
import org.egov.tradelicense.persistence.repository.PenaltyRateRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TradeLicenseApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@SuppressWarnings("rawtypes")
public class PenaltyRateServiceTest {

	@Autowired
	PenaltyRateService penaltyRateService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	PenaltyRateRepository penaltyRateRepository;

	@Autowired
	PenaltyRateConsumer penaltyRateConsumer;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "penaltyrate-create-validated",
			"penaltyrate-update-validated");

	public static Long penaltyRateId = 1l;
	public String tenantId = "default";
	public String applicationType = "New";
	public Long fromRange = -9999l;
	public Long toRange = 10l;
	public Long InvalidFromRange = 50l;
	public Long InvalidToRange = 100l;
	public Double rate = 50d;
	public Double rateforInvalidSeq = 51d;
	public Long updatedFromRange = 50l;
	public Long updatedToRange = 100l;

	@Before
	public void setUp() throws Exception {
		// wait until the partitions are assigned
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
				.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, 0);
		}
	}

	/**
	 * Description : test method to create PenaltyRate
	 */
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
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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

			penaltyRateConsumer.getLatch().await();
			if (penaltyRateConsumer.getLatch().getCount() != 0) {
				assertTrue(false);
			} else {
				Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
				Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
				penaltyRateResponse = penaltyRateService.getPenaltyRateMaster(requestInfo, tenantId, null,
						applicationType, pageSize, offset);

				if (penaltyRateResponse.getPenaltyRates().size() == 0) {
					assertTrue(false);
				} else {
					penaltyRateId = penaltyRateResponse.getPenaltyRates().get(0).getId();
					assertTrue(true);
				}
			}

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method to check invalid range of PenaltyRate
	 */
	@Test
	public void testAcreatePenaltyRateInvalidSequence() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<PenaltyRate> penaltyRates = new ArrayList<>();

		PenaltyRate penaltyRate = new PenaltyRate();
		penaltyRate.setTenantId(tenantId);
		penaltyRate.setApplicationType(ApplicationTypeEnum.fromValue(applicationType));
		penaltyRate.setFromRange(InvalidFromRange);
		penaltyRate.setToRange(InvalidToRange);
		penaltyRate.setRate(rateforInvalidSeq);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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

		} catch (Exception e) {
			if (e.getClass().isInstance(new InvalidRangeException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : test method to search PenaltyRate
	 */

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
			if (penaltyRateResponse.getPenaltyRates().size() == 0) {
				assertTrue(false);
			}

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method to update PenaltyRate
	 */

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
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		penaltyRate.setAuditDetails(auditDetails);
		penaltyRates.add(penaltyRate);

		PenaltyRateRequest penaltyRateRequest = new PenaltyRateRequest();
		penaltyRateRequest.setPenaltyRates(penaltyRates);
		penaltyRateRequest.setRequestInfo(requestInfo);

		try {

			penaltyRateConsumer.resetCountDown();

			PenaltyRateResponse penaltyRateResponse = penaltyRateService.updatePenaltyRateMaster(penaltyRateRequest);

			penaltyRateConsumer.getLatch().await();

			if (penaltyRateResponse.getPenaltyRates().size() == 0) {
				assertTrue(false);
			}

			penaltyRateRepository.updatePenaltyRate(penaltyRateResponse.getPenaltyRates().get(0));
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
		Integer userId = 1;
		userInfo.setUserName(username);
		userInfo.setId(userId);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}
}