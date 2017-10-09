package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.requests.LicenseStatusRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.consumers.LicenseStatusConsumer;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.services.LicenseStatusService;
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
public class LicenseStatusServiceTest {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	LicenseStatusService licenseStatusService;

	@Autowired
	LicenseStatusConsumer licenseStatusConsumer;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "licensestatus-create-validated",
			"licensestatus-update-validated");

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	public static Long licenseId = 1l;
	public String tenantId = "default";
	public String name = "Shubham";
	public String code = "babu";
	public String NameToupdate = "shubhamPratap";
	public String CodeToUpdate = "nitin";
	public String active = "True";
	public String moduleType = "2342567";

	@Before
	public void setUp() throws Exception {
		// wait until the partitions are assigned
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
				.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, 0);
		}
	}

	/**
	 * Description : Test method for createLicenceStatus() method
	 */
	@Test
	public void testAcreateLicenseStatus() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<LicenseStatus> licenseStatuslst = new ArrayList<LicenseStatus>();

		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setTenantId(tenantId);
		licenseStatus.setName(name);
		licenseStatus.setCode(code);
		licenseStatus.setActive(Boolean.valueOf(active));
		licenseStatus.setModuleType(moduleType);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		licenseStatus.setAuditDetails(auditDetails);
		licenseStatuslst.add(licenseStatus);

		LicenseStatusRequest licenseStatusRequest = new LicenseStatusRequest();
		licenseStatusRequest.setLicenseStatuses(licenseStatuslst);
		licenseStatusRequest.setRequestInfo(requestInfo);

		try {
			LicenseStatusResponse licenseStatusResponse = licenseStatusService
					.createLicenseStatusMaster(licenseStatusRequest);
			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}

			licenseStatusConsumer.getLatch().await();
			if (licenseStatusConsumer.getLatch().getCount() != 0) {
				assertTrue(false);
			} else {
				Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
				Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
				licenseStatusResponse = licenseStatusService.getLicenseStatusMaster(requestInfo, tenantId, null,new String[]{ code}, name,
	 moduleType, active, pageSize, offset);

				if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
					assertTrue(false);
				} else {
					licenseId = licenseStatusResponse.getLicenseStatuses().get(0).getId();
					assertTrue(true);
				}
			}
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : Test method for searchLicenceStatus() method
	 */
	@Test
	public void testAsearchLicenseStatus() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			LicenseStatusResponse licenseStatusResponse = licenseStatusService.getLicenseStatusMaster(requestInfo,
					tenantId, new Integer[] { licenseId.intValue() },new String[]{ code}, name,  moduleType, active, pageSize, offset);

			if (licenseStatusResponse.getLicenseStatuses().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : Test method for createLicenceStatus() method to check
	 * duplicate records
	 */
	@Test
	public void testAcreateLicenseStatusDuplicate() {

		RequestInfo requestInfo = getRequestInfoObject();
		List<LicenseStatus> licenseStatuses = new ArrayList<LicenseStatus>();

		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setTenantId(tenantId);
		licenseStatus.setName(name);
		licenseStatus.setCode(code);
		licenseStatus.setModuleType(moduleType);
		licenseStatus.setActive(Boolean.valueOf(active));
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		licenseStatus.setAuditDetails(auditDetails);
		licenseStatuses.add(licenseStatus);

		LicenseStatusRequest licenseStatusRequest = new LicenseStatusRequest();
		licenseStatusRequest.setLicenseStatuses(licenseStatuses);
		licenseStatusRequest.setRequestInfo(requestInfo);

		try {
			LicenseStatusResponse licenseStatusResponse = licenseStatusService
					.createLicenseStatusMaster(licenseStatusRequest);

			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}

		} catch (Exception e) {
			if (e instanceof DuplicateIdException) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : Test method for updateLicenceStatus() method to check name
	 * modification
	 */
	@Test
	public void testBmodifyLicenseName() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<LicenseStatus> LicenseStatuses = new ArrayList<LicenseStatus>();

		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setId(licenseId);
		licenseStatus.setTenantId(tenantId);
		licenseStatus.setName(NameToupdate);
		licenseStatus.setCode(code);
		licenseStatus.setModuleType(moduleType);
		licenseStatus.setActive(Boolean.valueOf(active));
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		licenseStatus.setAuditDetails(auditDetails);
		LicenseStatuses.add(licenseStatus);

		LicenseStatusRequest licenseStatusRequest = new LicenseStatusRequest();
		licenseStatusRequest.setLicenseStatuses(LicenseStatuses);
		licenseStatusRequest.setRequestInfo(requestInfo);

		try {

			licenseStatusConsumer.resetCountDown();

			LicenseStatusResponse licenseStatusResponse = licenseStatusService
					.updateLicenseStatusMaster(licenseStatusRequest);

			licenseStatusConsumer.getLatch().await();
			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);
		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : Test method for searchLicenceStatus() method to check
	 * modified search
	 */
//	@Test
//	public void testBsearchUpdatedLicenseName() {
//
//		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
//		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
//		RequestInfo requestInfo = getRequestInfoObject();
//
//		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
//		requestInfoWrapper.setRequestInfo(requestInfo);
//
//		try {
//			LicenseStatusResponse licenseStatusResponse = licenseStatusService.getLicenseStatusMaster(requestInfo,
//					tenantId, null, NameToupdate, CodeToUpdate, moduleType, active, pageSize, offset);
//
//			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
//				assertTrue(false);
//			}
//
//			licenseStatusConsumer.getLatch().await();
//			if (licenseStatusConsumer.getLatch().getCount() != 0) {
//				assertTrue(false);
//			} else {
//				assertTrue(true);
//			}
//
//		} catch (Exception e) {
//			assertTrue(false);
//		}
//
//	}

	/**
	 * Description : Test method for updateLicenceStatus() method to modify code
	 */
	@Test
	public void testCmodifyLiceanceCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<LicenseStatus> licenseStatuses = new ArrayList<LicenseStatus>();

		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setId(licenseId);
		licenseStatus.setTenantId(tenantId);
		licenseStatus.setName(NameToupdate);
		licenseStatus.setCode(CodeToUpdate);
		licenseStatus.setModuleType(moduleType);
		licenseStatus.setActive(Boolean.valueOf(active));
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		licenseStatus.setAuditDetails(auditDetails);
		licenseStatuses.add(licenseStatus);

		LicenseStatusRequest LicenseStatusRequest = new LicenseStatusRequest();
		LicenseStatusRequest.setLicenseStatuses(licenseStatuses);
		LicenseStatusRequest.setRequestInfo(requestInfo);

		try {

			licenseStatusConsumer.resetCountDown();

			LicenseStatusResponse licenseStatusResponse = licenseStatusService
					.updateLicenseStatusMaster(LicenseStatusRequest);

			licenseStatusConsumer.getLatch().await();
			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : Test method for updateLicenceStatus() method
	 */
	@Test
	public void testCmodifyLicenseCodeDuplicate() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<LicenseStatus> LicenseStatuses = new ArrayList<LicenseStatus>();

		LicenseStatus licenseStatus = new LicenseStatus();
		licenseStatus.setId(licenseId + 1);
		licenseStatus.setTenantId(tenantId);
		licenseStatus.setName(NameToupdate);
		licenseStatus.setCode(CodeToUpdate);
		licenseStatus.setModuleType(moduleType);
		licenseStatus.setActive(Boolean.valueOf(active));
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		licenseStatus.setAuditDetails(auditDetails);
		LicenseStatuses.add(licenseStatus);

		LicenseStatusRequest licenseStatusRequest = new LicenseStatusRequest();
		licenseStatusRequest.setLicenseStatuses(LicenseStatuses);
		licenseStatusRequest.setRequestInfo(requestInfo);

		try {
			LicenseStatusResponse licenseStatusResponse = licenseStatusService
					.updateLicenseStatusMaster(licenseStatusRequest);

			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}

		} catch (Exception e) {
			if (e instanceof DuplicateIdException) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : Test method for updateLicenceStatus() method
	 */
	@Test
	public void testCsearchUpdatedLicenseCode() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			LicenseStatusResponse licenseStatusResponse = licenseStatusService.getLicenseStatusMaster(requestInfo,
					tenantId, new Integer[] { licenseId.intValue() }, new String[]{ CodeToUpdate},NameToupdate, moduleType, active, pageSize,
					offset);
			if (licenseStatusResponse.getLicenseStatuses().size() == 0) {
				assertTrue(false);
			}

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
		UserInfo userInfo = new UserInfo();
		String username = "pavan";
		Integer userId = 1;
		userInfo.setUserName(username);
		userInfo.setId(userId);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}

}
