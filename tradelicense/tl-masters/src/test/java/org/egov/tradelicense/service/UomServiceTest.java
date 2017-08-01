package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.UOM;
import org.egov.models.UOMRequest;
import org.egov.models.UOMResponse;
import org.egov.models.UserInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.services.UOMService;
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
public class UomServiceTest {

	@Autowired
	UOMService uomService;

	@Autowired
	private PropertiesManager propertiesManager;

	public Long uomId = 1l;
	public String tenantId = "default";
	public String name = "Flammables v1.1";
	public String code = "Flammables v1.1";
	public Boolean active = true;
	public String updatedName = "Flammables v1.1 name updated";
	public String updatedCode = "Flammables v1.1 code updated";

	@Test
	public void testAcreateUom() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<UOM> uoms = new ArrayList<>();

		UOM uom = new UOM();
		uom.setTenantId(tenantId);
		uom.setName(name);
		uom.setCode(code);
		uom.setActive(active);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		uom.setAuditDetails(auditDetails);
		uoms.add(uom);

		UOMRequest uomRequest = new UOMRequest();
		uomRequest.setUoms(uoms);
		uomRequest.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.createUomMaster( uomRequest);
			if (uomResponse.getUoms().size() == 0) {
				assertTrue(false);
			}
			this.uomId = uomResponse.getUoms().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testAsearchUom() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.getUomMaster(requestInfo, tenantId, new Integer[] { uomId.intValue() },
					name, code, active, pageSize, offset);
			if (uomResponse.getUoms().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testAcreateUomDuplicate() {

		RequestInfo requestInfo = getRequestInfoObject();
		List<UOM> uoms = new ArrayList<>();

		UOM uom = new UOM();
		uom.setTenantId(tenantId);
		uom.setName(name);
		uom.setCode(code);
		uom.setActive(active);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		uom.setAuditDetails(auditDetails);
		uoms.add(uom);

		UOMRequest uomRequest = new UOMRequest();
		uomRequest.setUoms(uoms);
		uomRequest.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.createUomMaster( uomRequest);
			if (uomResponse.getUoms().size() == 0) {
				assertTrue(false);
			}
			this.uomId = uomResponse.getUoms().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	@Test
	public void testBmodifyUomName() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<UOM> uoms = new ArrayList<>();

		UOM uom = new UOM();
		uom.setId(uomId);
		uom.setTenantId(tenantId);
		uom.setName(updatedName);
		uom.setCode(code);
		uom.setActive(active);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		uom.setAuditDetails(auditDetails);
		uoms.add(uom);

		UOMRequest uomRequest = new UOMRequest();
		uomRequest.setUoms(uoms);
		uomRequest.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.updateUomMaster(uomRequest);

			if (uomResponse.getUoms().size() == 0)
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

	@Test
	public void testBsearchUpdatedUomName() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.getUomMaster(requestInfo, tenantId, new Integer[] { uomId.intValue() },
					updatedName, code, active, pageSize, offset);
			if (uomResponse.getUoms().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testCmodifyUomCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<UOM> uoms = new ArrayList<>();

		UOM uom = new UOM();
		uom.setId(uomId);
		uom.setTenantId(tenantId);
		uom.setName(updatedName);
		uom.setCode(updatedCode);
		uom.setActive(active);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		uom.setAuditDetails(auditDetails);
		uoms.add(uom);

		UOMRequest uomRequest = new UOMRequest();
		uomRequest.setUoms(uoms);
		uomRequest.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.updateUomMaster(uomRequest);

			if (uomResponse.getUoms().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testCmodifyUomCodeDuplicate() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<UOM> uoms = new ArrayList<>();

		UOM uom = new UOM();
		uom.setId(uomId);
		uom.setTenantId(tenantId);
		uom.setName(updatedName);
		uom.setCode(updatedCode);
		uom.setActive(active);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		uom.setAuditDetails(auditDetails);
		uoms.add(uom);

		UOMRequest uomRequest = new UOMRequest();
		uomRequest.setUoms(uoms);
		uomRequest.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.updateUomMaster(uomRequest);

			if (uomResponse.getUoms().size() == 0)
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

	@Test
	public void testCsearchUpdatedUomCode() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			UOMResponse uomResponse = uomService.getUomMaster(requestInfo, tenantId, new Integer[] { uomId.intValue() },
					updatedName, updatedCode, active, pageSize, offset);
			if (uomResponse.getUoms().size() == 0)
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
		UserInfo userInfo = new UserInfo();
		String username = "pavan";
		userInfo.setUsername(username);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}
}