package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.FeeMatrix;
import org.egov.tl.commons.web.contract.FeeMatrixDetail;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.FeeMatrixResponse;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.UOMRequest;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.consumers.FeeMatrixConsumer;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.services.FeeMatrixService;
import org.egov.tradelicense.persistence.repository.CategoryRepository;
import org.egov.tradelicense.persistence.repository.FeeMatrixRepository;
import org.egov.tradelicense.persistence.repository.UOMRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TradeLicenseApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@SuppressWarnings("rawtypes")
public class FeeMatrixServiceTest {

	@Autowired
	FeeMatrixService feeMatrixService;

	@Autowired
	FeeMatrixRepository feeMatrixRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	FeeMatrixConsumer feeMatrixConsumer;

	@Autowired
	CategoryRepository CategoryRepository;

	@Autowired
	UOMRepository uomRepository;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "penaltyrate-create-validated",
			"penaltyrate-update-validated");

	@Before
	public void setUp() throws Exception {
		// wait until the partitions are assigned
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
				.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, 0);
		}
	}

	public static Long uomId = 0L;
	public static Long categoryId = 0L;
	public static Long categoryDetailsId = 0L;

	public static Long feeMatrixId = 1l;
	public String tenantId = "default";
	public String applicationType = "NEW";
	public String applicationTyperenew = "RENEW";
	public Long fromRange = -9999l;
	public Long toRange = 10l;
	public Long InvalidFromRange = 50l;
	public Long InvalidToRange = 100l;
	public Long updatedFromRange = 50l;
	public Long updatedToRange = 100l;
	public static String businessNature = BusinessNatureEnum.PERMANENT.name();
	public static String businessNatureupdate = BusinessNatureEnum.TEMPORARY.name();

	public static String financialYear = "3";
	public static String invalideffectiveFrom = "27/07/2017";
	public static String effectiveFrom = "29/07/2017";
	public static String effectiveTo = "29/07/2017";

	/**
	 * Description : test method to create FeeMatrix
	 */
	@Test
	public void testAcreateFeeMatrix() {
		insertRequiredValues();
		RequestInfo requestInfo = getRequestInfoObject();

		List<FeeMatrix> feeMatrixcs = new ArrayList<>();
		List<FeeMatrixDetail> feeMatrixcdetails = new ArrayList<>();
		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId(tenantId);
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(applicationType));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(businessNature));
		feeMatrix.setSubCategoryId(categoryDetailsId);
		feeMatrix.setCategoryId(categoryId);
		feeMatrix.setEffectiveFrom(effectiveFrom);
		feeMatrix.setFinancialYear(financialYear);
		feeMatrix.setEffectiveTo(effectiveTo);
		FeeMatrixDetail feeMatrixDetails = new FeeMatrixDetail();
		feeMatrixDetails.setFeeMatrixId(1l);
		feeMatrixDetails.setUomFrom(0l);
		feeMatrixDetails.setUomTo(20l);
		feeMatrixDetails.setAmount(Double.valueOf(1));
		feeMatrixcdetails.add(feeMatrixDetails);
		feeMatrix.setFeeMatrixDetails(feeMatrixcdetails);
		long createdTime = new Date().getTime();
		String token = getToken();
		if (token != null) {
			requestInfo.setAuthToken(token);
		}
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		feeMatrix.setAuditDetails(auditDetails);
		feeMatrixcs.add(feeMatrix);

		FeeMatrixRequest feeMatrixRequest = new FeeMatrixRequest();
		feeMatrixRequest.setFeeMatrices(feeMatrixcs);
		feeMatrixRequest.setRequestInfo(requestInfo);

		try {
			FeeMatrixResponse feeMatrixResponse = feeMatrixService.createFeeMatrixMaster(feeMatrixRequest);

			if (feeMatrixResponse.getFeeMatrices().size() == 0) {
				assertTrue(false);
			}

			feeMatrixConsumer.getLatch().await();
			if (feeMatrixConsumer.getLatch().getCount() != 0) {
				assertTrue(false);
			} else {
				Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
				Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
				feeMatrixResponse = feeMatrixService.getFeeMatrixMaster(requestInfo, tenantId, null,
						categoryId.intValue(), categoryDetailsId.intValue(), financialYear, applicationType,
						businessNature, pageSize, offset);

				if (feeMatrixResponse.getFeeMatrices().size() == 0) {
					assertTrue(false);
				} else {
					feeMatrixId = feeMatrixResponse.getFeeMatrices().get(0).getId();
					assertTrue(true);
				}
			}

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method to create FeeMatrix
	 */
	@Test
	public void testBcreateFeeMatrixInvalidRange() {
		// insertRequiredValues();
		RequestInfo requestInfo = getRequestInfoObject();

		List<FeeMatrix> feeMatrixcs = new ArrayList<>();
		List<FeeMatrixDetail> feeMatrixcdetails = new ArrayList<>();
		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId(tenantId);
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(applicationTyperenew));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(businessNature));
		feeMatrix.setSubCategoryId(categoryDetailsId);
		feeMatrix.setCategoryId(categoryId);
		feeMatrix.setEffectiveFrom(invalideffectiveFrom);
		feeMatrix.setFinancialYear(financialYear);
		feeMatrix.setEffectiveTo(effectiveTo);
		FeeMatrixDetail feeMatrixDetails = new FeeMatrixDetail();
		feeMatrixDetails.setFeeMatrixId(1l);
		feeMatrixDetails.setUomFrom(0l);
		feeMatrixDetails.setUomTo(20l);
		feeMatrixDetails.setAmount(Double.valueOf(1));
		feeMatrixcdetails.add(feeMatrixDetails);
		FeeMatrixDetail feeMatrixDetails2 = new FeeMatrixDetail();
		feeMatrixDetails2.setFeeMatrixId(1l);
		feeMatrixDetails2.setUomFrom(0l);
		feeMatrixDetails2.setUomTo(20l);
		feeMatrixDetails2.setAmount(Double.valueOf(1));
		feeMatrixcdetails.add(feeMatrixDetails2);
		feeMatrix.setFeeMatrixDetails(feeMatrixcdetails);
		long createdTime = new Date().getTime();
		String token = getToken();
		if (token != null) {
			requestInfo.setAuthToken(token);
		}
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		feeMatrix.setAuditDetails(auditDetails);
		feeMatrixcs.add(feeMatrix);

		FeeMatrixRequest feeMatrixRequest = new FeeMatrixRequest();
		feeMatrixRequest.setFeeMatrices(feeMatrixcs);
		feeMatrixRequest.setRequestInfo(requestInfo);

		try {
			FeeMatrixResponse feeMatrixResponse = feeMatrixService.createFeeMatrixMaster(feeMatrixRequest);

			if (feeMatrixResponse.getFeeMatrices().size() == 0) {
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
	public void testCsearchFeeMatrix() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			FeeMatrixResponse feeMatrixResponse = feeMatrixService.getFeeMatrixMaster(requestInfo, tenantId, null,
					categoryId.intValue(), categoryDetailsId.intValue(), financialYear, applicationType, businessNature,
					pageSize, offset);
			if (feeMatrixResponse.getFeeMatrices().size() == 0) {
				assertTrue(false);
			} else {
				assertTrue(true);
			}

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : method to create requestInfo Object
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
		requestInfo.setAuthToken(getToken());
		UserInfo userInfo = new UserInfo();
		String username = "pavan";
		Integer userId = 1;
		userInfo.setUsername(username);
		userInfo.setId(userId);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}

	private Long insertInUom() {
		Long uomId = 0l;
		try {
			UOM uom = new UOM();
			uom.setTenantId("default");
			uom.setName("shubham1234");
			uom.setCode("shubham12564");
			uom.setActive(true);
			long createdTime = new Date().getTime();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("1");
			auditDetails.setLastModifiedBy("1");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);

			uom.setAuditDetails(auditDetails);
			RequestInfo requestInfo = getRequestInfoObject();
			UOMRequest uomRequest = new UOMRequest();
			List uoms = new ArrayList<UOM>();
			uoms.add(uom);
			uomRequest.setUoms(uoms);
			uomRequest.setRequestInfo(requestInfo);
			uomId = uomRepository.createUom(uomRequest.getUoms().get(0));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return uomId;
	}

	private Long createCategory() {
		Long categoryId = 0l;
		try {
			Category category = new Category();
			category.setTenantId("default");
			category.setName("pratap");
			category.setCode("1234");
			category.setParentId(null);
			category.setActive(false);
			category.setBusinessNature(BusinessNatureEnum.PERMANENT);
			long createdTime = new Date().getTime();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("shubham");
			auditDetails.setLastModifiedBy("nitin");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);

			category.setAuditDetails(auditDetails);
			categoryId = CategoryRepository.createCategory(category);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return categoryId;
	}

	@SuppressWarnings("unused")
	private Long createCategoryDetails() {

		Long categoryDetailsId = 0L;
		try {

			CategoryDetail details = new CategoryDetail();
			details.setFeeType(FeeTypeEnum.fromValue("License"));
			details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
			details.setUomId(uomId);
			details.setCategoryId(categoryId);

			long createdTime = new Date().getTime();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("1");
			auditDetails.setLastModifiedBy("1");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);
			details.setAuditDetails(auditDetails);

			categoryDetailsId = CategoryRepository.createCategoryDetail(details);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return categoryDetailsId;
	}

	public void insertRequiredValues() {
		uomId = insertInUom();
		categoryId = createCategory();
		categoryDetailsId = createCategoryDetails();
	}

	private String getToken() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0");

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", "narasappa");
		map.add("password", "demo");
		map.add("grant_type", "password");
		map.add("scope", "read");
		map.add("tenantId", "default");

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);

		String userAuthResponseInfo = restTemplate
				.postForObject("http://egov-micro-dev.egovernments.org/user/oauth/token", requestEntity, String.class);
		ObjectMapper mapper = new ObjectMapper();
		String token = null;
		if (userAuthResponseInfo != null) {
			Map mapToken = null;
			try {
				mapToken = mapper.readValue(userAuthResponseInfo, Map.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			token = String.valueOf(mapToken.get("access_token"));
		}
		return token;

	}

	/**
	 * Description : test method to create FeeMatrix
	 */
	@Test
	public void testDUpdateFeeMatrix() {

		RequestInfo requestInfo = getRequestInfoObject();

		List<FeeMatrix> feeMatrixcs = new ArrayList<>();
		List<FeeMatrixDetail> feeMatrixcdetails = new ArrayList<>();
		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setTenantId(tenantId);
		feeMatrix.setId(feeMatrixId);
		feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(applicationTyperenew));
		feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(businessNatureupdate));
		feeMatrix.setSubCategoryId(categoryDetailsId);
		feeMatrix.setCategoryId(categoryId);
		feeMatrix.setEffectiveFrom(effectiveFrom);
		feeMatrix.setFinancialYear(financialYear);
		feeMatrix.setEffectiveTo(effectiveTo);
		FeeMatrixDetail feeMatrixDetails = new FeeMatrixDetail();
		feeMatrixDetails.setFeeMatrixId(1l);
		feeMatrixDetails.setUomFrom(0l);
		feeMatrixDetails.setUomTo(20l);
		feeMatrixDetails.setAmount(Double.valueOf(1));
		feeMatrixcdetails.add(feeMatrixDetails);
		feeMatrix.setFeeMatrixDetails(feeMatrixcdetails);
		long createdTime = new Date().getTime();
		String token = getToken();
		if (token != null) {
			requestInfo.setAuthToken(token);
		}
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		feeMatrix.setAuditDetails(auditDetails);
		feeMatrixcs.add(feeMatrix);

		FeeMatrixRequest feeMatrixRequest = new FeeMatrixRequest();
		feeMatrixRequest.setFeeMatrices(feeMatrixcs);
		feeMatrixRequest.setRequestInfo(requestInfo);

		try {
			// feeMatrixConsumer.resetCountDown();
			FeeMatrixResponse feeMatrixResponse = feeMatrixService.updateFeeMatrixMaster(feeMatrixRequest);

			feeMatrixConsumer.getLatch().await();

			if (feeMatrixConsumer.getLatch().getCount() != 0) {
				assertTrue(false);
			}

			feeMatrixRepository.updateFeeMatrix(feeMatrixRequest.getFeeMatrices().get(0));
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

}
