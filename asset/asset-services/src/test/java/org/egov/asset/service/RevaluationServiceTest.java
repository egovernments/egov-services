package org.egov.asset.service;

import static org.junit.Assert.assertEquals;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.Location;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.RevaluationStatus;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RevaluationService.class)
// @RunWith(MockitoJUnitRunner.class)
public class RevaluationServiceTest {

	@Mock
	private RevaluationRepository revaluationRepository;

	@Mock
	private AssetProducer assetProducer;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private RevaluationCriteria revaluationCriteria;

	@Mock
	private AssetCurrentAmountService assetCurrentAmountService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private RevaluationRequest revaluationRequest;

	@Mock
	private VoucherService voucherService;

	@InjectMocks
	private RevaluationService revaluationService;

	@Mock
	private ChartOfAccountDetailContract chartOfAccountDetailContract;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
}

	@Test
	public void testCreate() {
		RevaluationResponse revaluationResponse = null;
		try {
			revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		RevaluationRequest revaluationRequest = new RevaluationRequest();
		revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
		revaluationRequest.getRevaluation().setId(Long.valueOf("15"));

		doNothing().when(revaluationRepository).create(revaluationRequest);
		revaluationService.create(Matchers.any(RevaluationRequest.class));
		assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
				revaluationRequest.getRevaluation().toString());
	}

	@Test
	public void testCreateAsync() throws NumberFormatException, Exception {

		when(assetCurrentAmountService.getAsset(any(Long.class), any(String.class), any(RequestInfo.class))).thenReturn(get_Asset());
		//RevaluationService spy = PowerMockito.spy(new RevaluationService());
		RevaluationService mock = PowerMockito.mock(RevaluationService.class);
		PowerMockito.doReturn(Long.valueOf("6")).when(mock, "createVoucherForReevaluation",
				any(RevaluationRequest.class));

		RevaluationRequest revaluationRequest = new RevaluationRequest();
		revaluationRequest.setRevaluation(getRevaluationForCreateAsync());

		final List<RevaluationRequest> insertedRevaluationRequest = new ArrayList<>();
		insertedRevaluationRequest.add(revaluationRequest);
		RevaluationResponse revaluationResponse = null;
		try {
			revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		when(revaluationRepository.getNextRevaluationId()).thenReturn(Integer.valueOf("15"));
		revaluationRequest.getRevaluation().setId(Long.valueOf("15"));

		when(applicationProperties.getCreateAssetDisposalTopicName()).thenReturn("kafka.topics.save.disposal");

		// when(revaluationService.createVoucherForReevaluation(revaluationReq)).thenReturn(Long.valueOf("6"));

		assertTrue(revaluationResponse.getRevaluations().get(0).getId().equals(Long.valueOf("15")));
		doNothing().when(assetProducer).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		// revaluationService.createAsync(revaluationRequest);
		mock.createAsync(revaluationRequest);
		assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
				revaluationRequest.getRevaluation().toString());
	}

	@Test
	public void testSearch() {
		List<Revaluation> revaluation = new ArrayList<>();
		revaluation.add(getRevaluationForSearch());

		RevaluationResponse revaluationResponse = new RevaluationResponse();
		revaluationResponse.setRevaluations(revaluation);

		when(revaluationRepository.search(any(RevaluationCriteria.class))).thenReturn(revaluation);
		RevaluationResponse expectedRevaluationResponse = revaluationService.search(any(RevaluationCriteria.class));

		assertEquals(revaluationResponse.toString(), expectedRevaluationResponse.toString());
	}

	private List<ChartOfAccountDetailContract> get_ChartOfAccountDetailContract() {
		ChartOfAccountDetailContract chartOfAccountDetailContract = new ChartOfAccountDetailContract();

		List<ChartOfAccountDetailContract> chartOfAccountDetailContracts = new ArrayList<>();
		chartOfAccountDetailContracts.add(chartOfAccountDetailContract);

		return chartOfAccountDetailContracts;
	}

	private Revaluation getRevaluationForCreateAsync() {

		Revaluation revaluation = new Revaluation();
		revaluation.setTenantId("ap.kurnool");
		revaluation.setAssetId(Long.valueOf("31"));
		revaluation.setCurrentCapitalizedValue(Double.valueOf("100.68"));
		revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
		revaluation.setRevaluationAmount(Double.valueOf("10"));
		revaluation.setValueAfterRevaluation(Double.valueOf("90.68"));
		revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
		revaluation.setReevaluatedBy("5");
		revaluation.setReasonForRevaluation("reasonForRevaluation");
		revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
		revaluation.setFunction(Long.valueOf("2"));
		revaluation.setFund(Long.valueOf("3"));
		revaluation.setScheme(Long.valueOf("4"));
		revaluation.setSubScheme(Long.valueOf("5"));
		revaluation.setComments("coments");
		revaluation.setStatus(RevaluationStatus.ACTIVE);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
		revaluation.setAuditDetails(auditDetails);
		revaluation.setVoucherReference(Long.valueOf("42"));
		return revaluation;
	}

	private Asset get_Asset() {
		Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setId(Long.valueOf("31"));
		asset.setName("asset name");
		asset.setStatus(Status.CREATED);
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);

		Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");

		final AssetCategory assetCategory = new AssetCategory();
		assetCategory.setTenantId("ap.kurnool");
		assetCategory.setId(Long.valueOf("2"));
		assetCategory.setName("asset3");
		assetCategory.setCode(null);
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setParent(Long.valueOf("2"));
		assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
		assetCategory.setIsAssetAllow(true);
		assetCategory.setAccumulatedDepreciationAccount(Long.valueOf("1"));
		assetCategory.setDepreciationExpenseAccount(Long.valueOf("3"));
		assetCategory.setUnitOfMeasurement(Long.valueOf("10"));
		assetCategory.setVersion("v1");
		assetCategory.setDepreciationRate(null);
		assetCategory.setAssetAccount(Long.valueOf("2"));
		assetCategory.setRevaluationReserveAccount(Long.valueOf("2"));

		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		return asset;
	}

	private Revaluation getRevaluationForSearch() {

		Revaluation revaluation = new Revaluation();
		revaluation.setTenantId("ap.kurnool");
		revaluation.setAssetId(Long.valueOf("31"));
		revaluation.setCurrentCapitalizedValue(Double.valueOf("100.68"));
		revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
		revaluation.setRevaluationAmount(Double.valueOf("10"));
		revaluation.setValueAfterRevaluation(Double.valueOf("90.68"));
		revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
		revaluation.setReevaluatedBy("5");
		revaluation.setReasonForRevaluation("reasonForRevaluation");
		revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
		revaluation.setFunction(Long.valueOf("2"));
		revaluation.setFund(Long.valueOf("3"));
		revaluation.setScheme(Long.valueOf("4"));
		revaluation.setSubScheme(Long.valueOf("5"));
		revaluation.setComments("coments");
		revaluation.setStatus(RevaluationStatus.ACTIVE);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
		revaluation.setAuditDetails(auditDetails);

		return revaluation;
	}

	private RevaluationResponse getRevaluation(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, RevaluationResponse.class);
	}
}
