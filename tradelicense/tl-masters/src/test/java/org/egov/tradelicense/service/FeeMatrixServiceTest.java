package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.enums.BusinessNatureEnum;
import org.egov.models.AuditDetails;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.models.FeeMatrixRequest;
import org.egov.models.FeeMatrixResponse;
import org.egov.models.RequestInfo;
import org.egov.models.UserInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.services.FeeMatrixService;
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
public class FeeMatrixServiceTest {

	@Autowired
	FeeMatrixService feeMatrixService;

	@Autowired
	private PropertiesManager propertiesManager;

	public static Long feeMatrixId = 1l;
	public String tenantId = "default";
	String applicationType = "New";
	Long categoryId = 1l;
	String businessNature = "Permenant";
	Long subCategoryId = 2l;
	String financialYear = "2017-18";
	Long uomFrom = 0l;
	Long uomTo = 10l;
	Double amount = 100d;

	@Test
	public void testAcreateFeeMatrix() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<FeeMatrix> feeMatrices = new ArrayList<>();

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
		feeMatrixDetail.setUomFrom(uomFrom);
		feeMatrixDetail.setUomTo(uomTo);
		feeMatrixDetail.setAmount(amount);
		feeMatrixDetails.add(feeMatrixDetail);

		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId("default");
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(applicationType));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(businessNature));
		feeMatrix.setCategoryId(categoryId);
		feeMatrix.setSubCategoryId(subCategoryId);
		feeMatrix.setFinancialYear(financialYear);
		feeMatrix.setFeeMatrixDetails(feeMatrixDetails);

		long createdTime = new Date().getTime();
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrices.add(feeMatrix);

		FeeMatrixRequest feeMatrixRequest = new FeeMatrixRequest();
		feeMatrixRequest.setFeeMatrices(feeMatrices);
		feeMatrixRequest.setRequestInfo(requestInfo);

		try {
			FeeMatrixResponse feeMatrixResponse = feeMatrixService.createFeeMatrixMaster(tenantId, feeMatrixRequest);
			if (feeMatrixResponse.getFeeMatrices().size() == 0) {
				assertTrue(false);
			}
			this.feeMatrixId = feeMatrixResponse.getFeeMatrices().get(0).getId();

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