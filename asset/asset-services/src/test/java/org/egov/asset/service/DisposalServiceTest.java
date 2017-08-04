package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DisposalService.class)
public class DisposalServiceTest {

    @Mock
    private DisposalRepository disposalRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

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
        final List<Disposal> disposal = new ArrayList<>();
        disposal.add(getDisposalForSearch());

        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setDisposals(disposal);

        final RequestInfo requestInfo = new RequestInfo();
        when(disposalRepository.search(any(DisposalCriteria.class))).thenReturn(disposal);
        final DisposalResponse expectedDisposalResponse = disposalService.search(Matchers.any(DisposalCriteria.class),
                requestInfo);

        assertEquals(disposalResponse.toString(), expectedDisposalResponse.toString());
    }

    @Test
    public void testCreateAsync() throws NumberFormatException, Exception {

        final DisposalService mock = PowerMockito.mock(DisposalService.class);
        PowerMockito.doReturn(Long.valueOf("6")).when(mock, "createVoucherForDisposal", any(DisposalRequest.class),
                any(HttpHeaders.class));

        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setDisposal(getDisposalForCreateAsync());
        disposalRequest.getDisposal().setVoucherReference(Long.valueOf("6"));

        final List<DisposalRequest> insertedDisposalRequest = new ArrayList<>();
        insertedDisposalRequest.add(disposalRequest);
        DisposalResponse disposalResponse = null;
        try {
            disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }

        when(assetCurrentAmountService.getAsset(any(Long.class), any(String.class), any(RequestInfo.class)))
                .thenReturn(get_Asset());
        when(applicationProperties.getCreateAssetDisposalTopicName()).thenReturn("kafka.topics.save.disposal");
        when(disposalRepository.getNextDisposalId()).thenReturn(15);

        assertTrue(disposalResponse.getDisposals().get(0).getId().equals(Long.valueOf("15")));
        // doNothing().when(logAwareKafkaTemplate).send(Matchers.anyString(),
        // Matchers.anyString(), Matchers.anyObject());
        mock.createAsync(disposalRequest, new HttpHeaders());

        assertEquals(disposalResponse.getDisposals().get(0).getVoucherReference(),
                disposalRequest.getDisposal().getVoucherReference());
    }

    @Test
    public void testCreate() {
        final DisposalService mock = PowerMockito.mock(DisposalService.class);
        DisposalResponse disposalResponse = null;
        try {
            disposalResponse = getDisposal("disposal/disposalServiceResponse.disposal1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setDisposal(getDisposalForCreateAsync());
        disposalRequest.getDisposal().setId(Long.valueOf("15"));
        disposalRequest.getDisposal().setVoucherReference(Long.valueOf("6"));

        doNothing().when(disposalRepository).create(disposalRequest);
        mock.create(disposalRequest);
        assertEquals(disposalResponse.getDisposals().get(0).toString(), disposalRequest.getDisposal().toString());
    }

    private Disposal getDisposalForSearch() {

        final Disposal disposal = new Disposal();
        disposal.setTenantId("ap.kurnool");
        disposal.setId(Long.valueOf("15"));
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setDisposalReason("disposalReason");
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("15"));
        disposal.setAuditDetails(getAuditDetails());

        return disposal;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("2");
        auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
        auditDetails.setLastModifiedBy("2");
        auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
        return auditDetails;
    }

    private Disposal getDisposalForCreateAsync() {

        final Disposal disposal = new Disposal();
        disposal.setTenantId("ap.kurnool");
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setDisposalReason("disposalReason");
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("15"));
        disposal.setVoucherReference(Long.valueOf("6"));
        disposal.setAuditDetails(getAuditDetails());

        return disposal;
    }

    private Asset get_Asset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setId(Long.valueOf("31"));
        asset.setName("asset name");
        asset.setStatus("CREATED");
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);

        final Location location = new Location();
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

    private DisposalResponse getDisposal(final String filePath) throws IOException {
        final String empJson = new FileUtils().getFileContents(filePath);
        return new ObjectMapper().readValue(empJson, DisposalResponse.class);
    }
}
