package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
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
import org.egov.tl.masters.domain.service.FeeMatrixService;
import org.egov.tl.masters.persistence.queue.FeeMatrixQueueRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.configuration.TestConfiguration;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class FeeMatrixServiceTest {

	@InjectMocks
	FeeMatrixService feeMatrixService;

	@Mock
	FeeMatrixDomainRepository feeMatrixDomainRepository;

	@Mock
	FeeMatrixDetailDomainRepository feeMatrixDetailDomainRepository;

	@Mock
	FinancialRepository financialRepository;

	@Mock
	FeeMatrixQueueRepository feeMatrixQueueRepository;

	@Mock
	PropertiesManager propertiesManager;

	@Before
	public void setup() {
	}

	@Test
	public final void testCreate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.TRUE);
		when(feeMatrixDomainRepository.validateCategory(any(String.class), any(String.class), any(String.class)))
				.thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.checkUniquenessOfFeeMatrix(any(String.class), any(ApplicationTypeEnum.class),
				any(FeeTypeEnum.class), any(BusinessNatureEnum.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(Boolean.FALSE);
		when(financialRepository.findFinancialYearByFinRange(any(String.class), any(String.class),
				any(RequestInfoWrapper.class))).thenReturn(getFinancialYear());
		when(financialRepository.findFinancialYearById(any(String.class), any(String.class),
				any(RequestInfoWrapper.class))).thenReturn(getFinancialYear());
		when(feeMatrixDetailDomainRepository.getFeeDetailMatrixNextSequence()).thenReturn(1l);

		List<FeeMatrix> actualResult = feeMatrixService.createFeeMatrixMaster(expectedResult, getRequestInfo());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public final void testUpdate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.FALSE);

		when(feeMatrixDomainRepository.validateInputId(any(Long.class), any(String.class))).thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.getFeeMatrixById(any(Long.class), any(String.class))).thenReturn(getFeeMatrix());
		when(feeMatrixDetailDomainRepository.getFeeMatrixDetailsByFeeMatrixId(any(Long.class)))
				.thenReturn(getFeeMatrixDetails());
		when(financialRepository.findFinancialYearByFinRange(any(String.class), any(String.class),
				any(RequestInfoWrapper.class))).thenReturn(getFinancialYear());
		when(feeMatrixDomainRepository.getFeeMatrixById(any(Long.class),any(String.class))).thenReturn(getUpdateFeeMatrix());
		List<FeeMatrix> actualResult = feeMatrixService.updateFeeMatrixMaster(expectedResult, getRequestInfo());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public final void testSearch() {
		FeeMatrixSearchCriteria expectedResult = getFeeMatrixSearchCriteria();

		when(feeMatrixDomainRepository.search(any(FeeMatrixSearchCriteria.class), any(RequestInfo.class)))
				.thenReturn(getFeeMatrixSearch());
		when(financialRepository.findFinancialYearByFinRange(any(String.class), any(String.class),
				any(RequestInfoWrapper.class))).thenReturn(getFinancialYear());
		List<FeeMatrixSearch> actualResult = feeMatrixService.search(expectedResult, getRequestInfo());

		assertEquals(expectedResult.getApplicationType(), actualResult.get(0).getApplicationType());
		assertEquals(expectedResult.getCategory(), actualResult.get(0).getCategory());
	}

	@Test
	public final void test_Save() {
		FeeMatrix expectedResult = getFeeMatrix();
		when(feeMatrixDomainRepository.add(any(FeeMatrix.class))).thenReturn(expectedResult);
		FeeMatrix actualResult = feeMatrixService.save(getFeeMatrix());
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public final void test_Update() {
		FeeMatrix expectedResult = getFeeMatrix();
		when(feeMatrixDomainRepository.update(any(FeeMatrix.class))).thenReturn(expectedResult);
		FeeMatrix actualResult = feeMatrixService.update(getFeeMatrix());
		assertEquals(expectedResult, actualResult);
	}

	/*** NEGATIVE TEST CASES ***/
	@Test(expected = InvalidInputException.class)
	public final void testInvalidCategoryIdCreate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.TRUE);
		when(feeMatrixDomainRepository.validateCategory(any(String.class), any(String.class), any(String.class)))
				.thenReturn(Boolean.FALSE);
		feeMatrixService.createFeeMatrixMaster(expectedResult, getRequestInfo());
	}

	@Test(expected = InvalidInputException.class)
	public final void testExistedFeeMatrixCreate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.TRUE);
		when(feeMatrixDomainRepository.validateCategory(any(String.class), any(String.class), any(String.class)))
				.thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.checkUniquenessOfFeeMatrix(any(String.class), any(ApplicationTypeEnum.class),
				any(FeeTypeEnum.class), any(BusinessNatureEnum.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(Boolean.TRUE);
		feeMatrixService.createFeeMatrixMaster(expectedResult, getRequestInfo());
	}

	@Test(expected = InvalidInputException.class)
	public final void testInvalidFeeMatrixDetailsRangeCreate() {
		List<FeeMatrix> expectedResult = getInvalidFeeMatrices();
		when(feeMatrixDomainRepository.validateCategory(any(String.class), any(String.class), any(String.class)))
				.thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.checkUniquenessOfFeeMatrix(any(String.class), any(ApplicationTypeEnum.class),
				any(FeeTypeEnum.class), any(BusinessNatureEnum.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(Boolean.FALSE);
		feeMatrixService.createFeeMatrixMaster(expectedResult, getRequestInfo());
	}

	@Test(expected = InvalidInputException.class)
	public final void testInvalidIdUpdate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.FALSE);

		when(feeMatrixDomainRepository.validateInputId(any(Long.class), any(String.class))).thenReturn(Boolean.FALSE);

		List<FeeMatrix> actualResult = feeMatrixService.updateFeeMatrixMaster(expectedResult, getRequestInfo());

		assertEquals(expectedResult, actualResult);
	}

	@Test(expected = InvalidInputException.class)
	public final void testInvalidEqualityFeeMatrixUpdate() {
		List<FeeMatrix> expectedResult = getFeeMatrices(Boolean.FALSE);

		when(feeMatrixDomainRepository.validateInputId(any(Long.class), any(String.class))).thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.getFeeMatrixById(any(Long.class), any(String.class)))
				.thenReturn(getInvalidFeeMatrix());

		List<FeeMatrix> actualResult = feeMatrixService.updateFeeMatrixMaster(expectedResult, getRequestInfo());

		assertEquals(expectedResult, actualResult);
	}

	@Test(expected = InvalidInputException.class)
	public final void testInvalidFeeMatrixDetailsUpdate() {
		List<FeeMatrix> expectedResult = getInvalidFeeMatrices();

		when(feeMatrixDomainRepository.validateInputId(any(Long.class), any(String.class))).thenReturn(Boolean.TRUE);
		when(feeMatrixDomainRepository.getFeeMatrixById(any(Long.class), any(String.class))).thenReturn(getFeeMatrix());
		when(feeMatrixDetailDomainRepository.getFeeMatrixDetailsByFeeMatrixId(any(Long.class)))
				.thenReturn(getFeeMatrixDetails());
		when(financialRepository.findFinancialYearByFinRange(any(String.class), any(String.class),
				any(RequestInfoWrapper.class))).thenReturn(getFinancialYear());
		List<FeeMatrix> actualResult = feeMatrixService.updateFeeMatrixMaster(expectedResult, getRequestInfo());

		assertEquals(expectedResult, actualResult);
	}

	/**** MOCK OBJECT CREATION ****/
	private List<FeeMatrixSearch> getFeeMatrixSearch() {
		List<FeeMatrixSearch> feeMatrixSearch = new ArrayList<>();
		FeeMatrixSearch feeMatrix = new FeeMatrixSearch();

		feeMatrix.setId(1l);
		feeMatrix.setApplicationType("RENEW");
		feeMatrix.setTenantId("default");
		feeMatrix.setFeeType("LICENSE");
		feeMatrix.setCategory("test");
		feeMatrix.setSubCategory("testsub");
		feeMatrix.setFinancialYear("2");
		feeMatrix.setFeeMatrixDetails(getFeeMatrixDetails());

		feeMatrixSearch.add(feeMatrix);
		return feeMatrixSearch;
	}

	private FeeMatrixSearchCriteria getFeeMatrixSearchCriteria() {
		FeeMatrixSearchCriteria feeMatrixSearch = new FeeMatrixSearchCriteria();
		Integer[] ids = new Integer[1];
		ids[0] = 1;
		feeMatrixSearch.setIds(ids);
		feeMatrixSearch.setFallBack(true);
		feeMatrixSearch.setTenantId("default");
		feeMatrixSearch.setApplicationType("RENEW");
		feeMatrixSearch.setCategory("test");
		feeMatrixSearch.setSubCategory("testsub");
		feeMatrixSearch.setFinancialYear("2");

		return feeMatrixSearch;
	}

	private List<FeeMatrix> getFeeMatrices(Boolean type) {
		List<FeeMatrix> feeMatrices = new ArrayList<FeeMatrix>();
		FeeMatrix feeMatrix = FeeMatrix.builder().id(1l).applicationType(ApplicationTypeEnum.NEW).tenantId("default")
				.financialYear("2").category("test").subCategory("testsub").feeType(FeeTypeEnum.LICENSE).build();
		FeeMatrixDetail feeMatrixDetail1 = FeeMatrixDetail.builder().id(1l).amount(type ? 100.00 : 150.00).uomFrom(0l)
				.uomTo(type ? 10l : 20l).build();
		FeeMatrixDetail feeMatrixDetail2 = FeeMatrixDetail.builder().id(2l).amount(type ? 200.00 : 250.00)
				.uomFrom(type ? 10l : 20l).uomTo(type ? 30l : 40l).build();

		List<FeeMatrixDetail> feeMatrixDetail = new ArrayList<>();
		feeMatrixDetail.add(feeMatrixDetail1);
		feeMatrixDetail.add(feeMatrixDetail2);

		feeMatrix.setFeeMatrixDetails(feeMatrixDetail);
		feeMatrices.add(feeMatrix);
		return feeMatrices;
	}

	private List<FeeMatrixDetail> getFeeMatrixDetails() {
		FeeMatrixDetail feeMatrixDetail1 = FeeMatrixDetail.builder().id(1l).amount(100.00).uomFrom(0l).uomTo(10l)
				.build();
		FeeMatrixDetail feeMatrixDetail2 = FeeMatrixDetail.builder().id(2l).amount(200.00).uomFrom(10l).uomTo(30l)
				.build();

		List<FeeMatrixDetail> feeMatrixDetail = new ArrayList<>();
		feeMatrixDetail.add(feeMatrixDetail1);
		feeMatrixDetail.add(feeMatrixDetail2);

		return feeMatrixDetail;
	}

	private FeeMatrix getFeeMatrix() {
		return FeeMatrix.builder().id(1l).applicationType(ApplicationTypeEnum.NEW).tenantId("default")
				.financialYear("2").category("test").subCategory("testsub").feeType(FeeTypeEnum.LICENSE).build();
	}
	
	private FeeMatrix getUpdateFeeMatrix() {
		return FeeMatrix.builder().id(1l).applicationType(ApplicationTypeEnum.NEW).tenantId("default")
				.financialYear("1").category("test").subCategory("testsub").feeType(FeeTypeEnum.LICENSE).build();
	}

	private FinancialYearContract getFinancialYear() {
		FinancialYearContract financialYear = new FinancialYearContract();
		financialYear.setId(1l);
		financialYear.setActive(true);
		financialYear.setStartingDate(new Date(1505815561));
		financialYear.setEndingDate(new Date(158476896l));
		financialYear.setFinYearRange("2016-17");
		financialYear.setIsClosed(false);

		return financialYear;
	}

	private FeeMatrix getInvalidFeeMatrix() {
		return FeeMatrix.builder().id(1l).applicationType(ApplicationTypeEnum.RENEW).tenantId("default")
				.financialYear("2").category("test").subCategory("testsub").feeType(FeeTypeEnum.MOTOR).build();
	}

	private List<FeeMatrix> getInvalidFeeMatrices() {
		List<FeeMatrix> feeMatrices = new ArrayList<FeeMatrix>();
		FeeMatrix feeMatrix = FeeMatrix.builder().id(1l).applicationType(ApplicationTypeEnum.NEW).tenantId("default")
				.financialYear("2").category("test").subCategory("testsub").feeType(FeeTypeEnum.LICENSE).build();
		List<FeeMatrixDetail> feeMatrixDetail = getInvalidFeeMatrixDetails();

		feeMatrix.setFeeMatrixDetails(feeMatrixDetail);
		feeMatrices.add(feeMatrix);
		return feeMatrices;
	}

	private List<FeeMatrixDetail> getInvalidFeeMatrixDetails() {
		FeeMatrixDetail feeMatrixDetail1 = FeeMatrixDetail.builder().id(1l).amount(100.00).uomFrom(10l).uomTo(15l)
				.build();
		FeeMatrixDetail feeMatrixDetail2 = FeeMatrixDetail.builder().id(2l).amount(200.00).uomFrom(10l).uomTo(30l)
				.build();

		List<FeeMatrixDetail> feeMatrixDetail = new ArrayList<>();
		feeMatrixDetail.add(feeMatrixDetail1);
		feeMatrixDetail.add(feeMatrixDetail2);

		return feeMatrixDetail;
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

}
