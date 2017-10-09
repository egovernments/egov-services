package org.egov.tradelicense.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.masters.contract.repository.FinancialRepository;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
import org.egov.tl.masters.domain.model.FeeMatrixSearchCriteria;
import org.egov.tl.masters.domain.repository.FeeMatrixDetailDomainRepository;
import org.egov.tl.masters.domain.repository.FeeMatrixDomainRepository;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixSearchEntity;
import org.egov.tl.masters.persistence.queue.FeeMatrixQueueRepository;
import org.egov.tl.masters.persistence.repository.FeeMatrixJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.CategoryService;
import org.egov.tradelicense.persistence.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class FeeMatrixRepositoryTest {

	@InjectMocks
	FeeMatrixDomainRepository feeMatrixDomainRepository;

	@Mock
	FeeMatrixDetailDomainRepository feeMatrixDetailDomainRepository;

	@Mock
	FeeMatrixQueueRepository feeMatrixQueueRepository;

	@Mock
	FeeMatrixJdbcRepository feeMatrixJdbcRepository;

	@Mock
	ResponseInfoFactory responseInfoFactory;

	@Mock
	CategoryService categoryService;

	@Mock
	FinancialRepository financialRepository;

	@Mock
	CategoryRepository categoryRepository;

	@Mock
	PropertiesManager propertiesManager;

	@Mock
	JdbcTemplate jdbcTemplate;

	@Test
	public void testFeematrixCreate() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrix expectedResult = feeMatrixEntity.toDomain();
		when(feeMatrixJdbcRepository.create(any(FeeMatrixEntity.class))).thenReturn(feeMatrixEntity);
		FeeMatrix actualResult = feeMatrixDomainRepository.add(getFeeMatrixDomain(Boolean.TRUE));

		assertEquals(expectedResult.getApplicationType().name(), actualResult.getApplicationType().name());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
		assertEquals(expectedResult.getCategory(), actualResult.getCategory());
	}

	@Test
	public void testFeeMatrixUpdate() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.FALSE);
		FeeMatrix expectedResult = feeMatrixEntity.toDomain();
		when(feeMatrixJdbcRepository.update(any(FeeMatrixEntity.class))).thenReturn(feeMatrixEntity);
		FeeMatrix actualResult = feeMatrixDomainRepository.update(getFeeMatrixDomain(Boolean.FALSE));

		assertEquals(expectedResult.getApplicationType(), actualResult.getApplicationType());
		assertEquals(expectedResult.getBusinessNature(), actualResult.getBusinessNature());
	}

	@Test
	public void testFeeMatrixSearch() {
		List<FeeMatrixEntity> feeMatrixEntity = new ArrayList<>();
		FeeMatrixEntity entity = getFeeMatrixEntity();
		feeMatrixEntity.add(entity);

		CategorySearchResponse categorySearchResponse = new CategorySearchResponse();
		categorySearchResponse.setCategories(getCategories());
		categorySearchResponse.setResponseInfo(getResponseInfo());

		when(feeMatrixJdbcRepository.search(any(FeeMatrixSearchEntity.class))).thenReturn(feeMatrixEntity);
		when(categoryService.getCategoryMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
				any(String[].class), any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class),
				any(Integer.class))).thenReturn(categorySearchResponse);

		List<FeeMatrixSearch> actualResult = feeMatrixDomainRepository.search(getFeeMatrixSearchCriteria(),
				getRequestInfo());

		assertEquals(feeMatrixEntity.get(0).getApplicationType(), actualResult.get(0).getApplicationType());
		assertEquals(feeMatrixEntity.get(0).getFinancialYear(), actualResult.get(0).getFinancialYear());
	}

	/*** Negative Test cases ****/
	@Test(expected = NullPointerException.class)
	public void testFeematrixInvalidCreate() {
		feeMatrixDomainRepository.add(getInvalidFeeMatrixDomain());
	}

	@Test(expected = NullPointerException.class)
	public void testFeematrixInvalidUpdate() {
		feeMatrixDomainRepository.update(getInvalidFeeMatrixDomain());
	}

	/**** Mock Object creation ****/
	private FeeMatrixSearchCriteria getFeeMatrixSearchCriteria() {
		FeeMatrixSearchCriteria feeMatrixSearch = new FeeMatrixSearchCriteria();
		Integer[] ids = new Integer[1];
		ids[0] = 1;
		feeMatrixSearch.setIds(ids);
		feeMatrixSearch.setFallBack(true);
		feeMatrixSearch.setTenantId("default");
		feeMatrixSearch.setApplicationType("RENEW");
		feeMatrixSearch.setCategory("FLM");
		feeMatrixSearch.setSubCategory("FLMS");
		feeMatrixSearch.setFinancialYear("2016-17");

		return feeMatrixSearch;
	}

	private FeeMatrixSearchCriteria getInvalidFeeMatrixSearchCriteria() {
		FeeMatrixSearchCriteria feeMatrixSearch = new FeeMatrixSearchCriteria();
		Integer[] ids = new Integer[1];
		ids[0] = 1;
		feeMatrixSearch.setIds(ids);
		feeMatrixSearch.setTenantId("default");
		feeMatrixSearch.setApplicationType("NEW");

		return feeMatrixSearch;
	}

	private FeeMatrixEntity getFeeMatrixEntity() {
		FeeMatrixEntity feeMatrixSearch = new FeeMatrixEntity();
		feeMatrixSearch.setTenantId("default");
		feeMatrixSearch.setApplicationType("RENEW");
		feeMatrixSearch.setCategory("FLM");
		feeMatrixSearch.setSubCategory("FLMS");
		feeMatrixSearch.setFinancialYear("2016-17");
		feeMatrixSearch.setFeeType("LICENSE");
		feeMatrixSearch.setEffectiveFrom(new Timestamp(1502628723l));

		return feeMatrixSearch;
	}

	private FeeMatrixEntity getFeeMatrixEntity(Boolean type) {
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		FeeMatrix feeMatrix = getFeeMatrixDomain(type);

		feeMatrixEntity.setId(feeMatrix.getId());
		feeMatrixEntity.setTenantId(feeMatrix.getTenantId());
		feeMatrixEntity.setApplicationType(feeMatrix.getApplicationType().name());
		feeMatrixEntity.setBusinessNature(feeMatrix.getBusinessNature().name());
		feeMatrixEntity.setFeeType(feeMatrix.getFeeType().name());
		feeMatrixEntity.setFinancialYear(feeMatrix.getFinancialYear());
		feeMatrixEntity.setCategory(feeMatrix.getCategory());
		feeMatrixEntity.setSubCategory(feeMatrix.getSubCategory());
		feeMatrixEntity.setEffectiveFrom(new Timestamp(feeMatrix.getEffectiveFrom()));

		return feeMatrixEntity;
	}

	private FeeMatrix getFeeMatrixDomain(Boolean type) {
		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setId(1l);
		feeMatrix.setTenantId("default");
		feeMatrix.setApplicationType(type ? ApplicationTypeEnum.NEW : ApplicationTypeEnum.RENEW);
		feeMatrix.setBusinessNature(type ? BusinessNatureEnum.PERMANENT : BusinessNatureEnum.TEMPORARY);
		feeMatrix.setFeeType(FeeTypeEnum.LICENSE);
		feeMatrix.setFinancialYear("2");
		feeMatrix.setCategory("FLM");
		feeMatrix.setSubCategory("FLMS");
		feeMatrix.setEffectiveFrom(1502628723l);

		feeMatrix.setFeeMatrixDetails(getFeeMatrixDetails(type));

		return feeMatrix;

	}

	private List<FeeMatrixDetail> getFeeMatrixDetails(Boolean type) {
		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail1 = new FeeMatrixDetail();

		feeMatrixDetail1.setId(1l);
		feeMatrixDetail1.setFeeMatrixId(1l);
		feeMatrixDetail1.setAmount(type ? 100.00 : 150.00);
		feeMatrixDetail1.setTenantId("Default");
		feeMatrixDetail1.setUomFrom(0l);
		feeMatrixDetail1.setUomTo(type ? 10l : 15l);

		FeeMatrixDetail feeMatrixDetail2 = new FeeMatrixDetail();

		feeMatrixDetail2.setId(2l);
		feeMatrixDetail2.setFeeMatrixId(1l);
		feeMatrixDetail2.setAmount(type ? 200.00 : 250.00);
		feeMatrixDetail2.setTenantId("Default");
		feeMatrixDetail2.setUomFrom(type ? 10l : 15l);
		feeMatrixDetail2.setUomTo(type ? 20l : 30l);

		feeMatrixDetail1.setAuditDetails(getAuditDetails());
		feeMatrixDetail2.setAuditDetails(getAuditDetails());

		feeMatrixDetails.add(feeMatrixDetail1);
		feeMatrixDetails.add(feeMatrixDetail2);
		return feeMatrixDetails;
	}

	private AuditDetails getAuditDetails() {

		return AuditDetails.builder().createdBy("1").createdTime(12345678912l).lastModifiedBy("1")
				.lastModifiedTime(12345678912l).build();
	}

	private FeeMatrix getInvalidFeeMatrixDomain() {
		FeeMatrix feeMatrix = new FeeMatrix();
		feeMatrix.setApplicationType(ApplicationTypeEnum.NEW);
		feeMatrix.setBusinessNature(BusinessNatureEnum.PERMANENT);
		feeMatrix.setFinancialYear("2");
		feeMatrix.setCategory("FLM");
		feeMatrix.setSubCategory("FLMS");
		feeMatrix.setEffectiveFrom(1502628723l);

		return feeMatrix;

	}

	private RequestInfo getRequestInfo() {
		RequestInfo info = new RequestInfo();
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		info.setAction("create");
		info.setDid("did");
		info.setApiId("apiId");
		info.setKey("key");
		info.setMsgId("msgId");
		info.setTs(12345l);
		info.setUserInfo(userInfo);
		info.setAuthToken("null");
		return info;
	}

	private List<CategorySearch> getCategories() {
		List<CategorySearch> categorySearch = new ArrayList<>();
		CategorySearch category = new CategorySearch();
		category.setBusinessNature(org.egov.tl.commons.web.contract.enums.BusinessNatureEnum.TEMPORARY);
		category.setActive(true);
		category.setId(1l);

		List<CategoryDetailSearch> categoryDetailSearch = new ArrayList<>();
		CategoryDetailSearch categoryDetail = new CategoryDetailSearch();

		categoryDetail.setCategory("1");
		categoryDetail.setFeeType(org.egov.tl.commons.web.contract.enums.FeeTypeEnum.LICENSE);
		categoryDetail.setTenantId("default");
		categoryDetailSearch.add(categoryDetail);

		category.setDetails(categoryDetailSearch);
		categorySearch.add(category);

		return categorySearch;
	}

	private ResponseInfo getResponseInfo() {
		ResponseInfo info = new ResponseInfo();
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		info.setApiId("apiId");
		info.setMsgId("msgId");
		info.setTs(12345l);

		return info;
	}
}
