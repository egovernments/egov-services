package org.egov.asset.service;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestTemplate;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DisposalService.class)
public class DisposalServiceTest {

	@Mock
	private DisposalRepository disposalRepository;

	@Mock
	private AssetProducer assetProducer;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private DisposalCriteria disposalCriteria;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private AssetCurrentAmountService assetCurrentAmountService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private VoucherService voucherService;

	@Mock
	private ChartOfAccountDetailContract chartOfAccountDetailContract;

	@InjectMocks
	private DisposalService disposalService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearch() {
		List<Disposal> disposal = new ArrayList<>();
		disposal.add(getDisposalForSearch());

		DisposalResponse disposalResponse = new DisposalResponse();
		disposalResponse.setDisposals(disposal);

		RequestInfo requestInfo = new RequestInfo();
		when(disposalRepository.search(any(DisposalCriteria.class))).thenReturn(disposal);
		DisposalResponse expectedDisposalResponse = disposalService.search(Matchers.any(DisposalCriteria.class),
				requestInfo);

		assertEquals(disposalResponse.toString(), expectedDisposalResponse.toString());
	}

	@Test
	public void testCreateAsync() throws NumberFormatException, Exception {

		DisposalService mock = PowerMockito.mock(DisposalService.class);
		PowerMockito.doReturn(Long.valueOf("6")).when(mock, "createVoucherForDisposal", any(DisposalRequest.class));

		DisposalRequest disposalRequest = new DisposalRequest();
		disposalRequest.setDisposal(getDisposalForCreateAsync());
		disposalRequest.getDisposal().setVoucherReference(Long.valueOf("6"));

		final List<DisposalRequest> insertedDisposalRequest = new ArrayList<>();
		insertedDisposalRequest.add(disposalRequest);
		DisposalResponse disposalResponse = null;
		try {
			disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		when(assetCurrentAmountService.getAsset(any(Long.class), any(String.class), any(RequestInfo.class)))
				.thenReturn(get_Asset());
		when(applicationProperties.getCreateAssetDisposalTopicName()).thenReturn("kafka.topics.save.disposal");
		when(disposalRepository.getNextDisposalId()).thenReturn(15);

		assertTrue(disposalResponse.getDisposals().get(0).getId().equals(Long.valueOf("15")));
		doNothing().when(assetProducer).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		mock.createAsync(disposalRequest);

		assertEquals(disposalResponse.getDisposals().get(0).getVoucherReference(),
				disposalRequest.getDisposal().getVoucherReference());
	}

	@Test
	public void testCreate() {
		DisposalResponse disposalResponse = null;
		try {
			disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		DisposalRequest disposalRequest = new DisposalRequest();
		disposalRequest.setDisposal(getDisposalForCreateAsync());
		disposalRequest.getDisposal().setId(Long.valueOf("15"));
		disposalRequest.getDisposal().setVoucherReference(Long.valueOf("6"));

		doNothing().when(disposalRepository).create(disposalRequest);
		disposalService.create(Matchers.any(DisposalRequest.class));
		assertEquals(disposalResponse.getDisposals().get(0).toString(), disposalRequest.getDisposal().toString());
	}

	private Disposal getDisposalForSearch() {

		Disposal disposal = new Disposal();
		disposal.setTenantId("ap.kurnool");
		disposal.setId(Long.valueOf("15"));
		disposal.setAssetId(Long.valueOf("31"));
		disposal.setBuyerName("Abhi");
		disposal.setBuyerAddress("Bangalore");
		disposal.setDisposalDate(Long.valueOf("1496564536178"));
		disposal.setDisposalReason("disposalReason");
		disposal.setPanCardNumber("baq1234567");
		disposal.setAadharCardNumber("12345678123456");
		disposal.setAssetCurrentValue(Double.valueOf("100.0"));
		disposal.setSaleValue(Double.valueOf("200.0"));
		disposal.setTransactionType(TransactionType.SALE);
		disposal.setAssetSaleAccount(Long.valueOf("15"));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("2");
		auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
		auditDetails.setLastModifiedBy("2");
		auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
		disposal.setAuditDetails(auditDetails);

		return disposal;
	}

	private Disposal getDisposalForCreateAsync() {

		Disposal disposal = new Disposal();
		disposal.setTenantId("ap.kurnool");
		disposal.setAssetId(Long.valueOf("31"));
		disposal.setBuyerName("Abhi");
		disposal.setBuyerAddress("Bangalore");
		disposal.setDisposalDate(Long.valueOf("1496564536178"));
		disposal.setDisposalReason("disposalReason");
		disposal.setPanCardNumber("baq1234567");
		disposal.setAadharCardNumber("12345678123456");
		disposal.setAssetCurrentValue(Double.valueOf("100.0"));
		disposal.setSaleValue(Double.valueOf("200.0"));
		disposal.setTransactionType(TransactionType.SALE);
		disposal.setAssetSaleAccount(Long.valueOf("15"));
		disposal.setVoucherReference(Long.valueOf("6"));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("2");
		auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
		auditDetails.setLastModifiedBy("2");
		auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
		disposal.setAuditDetails(auditDetails);

		return disposal;
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

	private DisposalResponse getDisposal(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, DisposalResponse.class);
	}
}
