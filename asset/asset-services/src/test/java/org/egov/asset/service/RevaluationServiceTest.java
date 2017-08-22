package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.FundResponse;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Department;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Location;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.model.enums.VoucherType;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RevaluationServiceTest {

    @Mock
    private RevaluationRepository revaluationRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private RevaluationCriteria revaluationCriteria;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private RevaluationRequest revaluationRequest;

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private RevaluationService revaluationService;

    @Mock
    private ChartOfAccountDetailContract chartOfAccountDetailContract;

    @Mock
    private CurrentValueService currentValueService;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Test
    public void testCreate() {
        RevaluationResponse revaluationResponse = null;
        try {
            revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
        revaluationRequest.getRevaluation().setId(Long.valueOf("15"));

        doNothing().when(revaluationRepository).create(revaluationRequest);
        revaluationService.create(revaluationRequest);
        revaluationService.saveRevaluationAmountToCurrentAmount(revaluationRequest);
        assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
                revaluationRequest.getRevaluation().toString());
    }

    @Test
    public void testCreateAsync() throws Exception {
        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        final HttpHeaders headers = getHttpHeaders();
        final RevaluationResponse expectedRevaluationResponse = getRevaluationResponse();
        final RevaluationResponse actualRevaluationResponse = revaluationService.createAsync(revaluationRequest,
                headers);
        assertEquals(expectedRevaluationResponse.toString(), actualRevaluationResponse.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createVoucherForRevaluationTest() throws Exception {
        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        final HttpHeaders headers = getHttpHeaders();
        final Asset asset = getAsset();
        final List<Asset> assets = new ArrayList<>();
        assets.add(asset);
        final FunctionResponse functionResponse = getFunctionResponse();
        final FundResponse fundResponse = getFundResponse();
        final VoucherRequest voucherRequest = getVoucherRequest();
        when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
        when(voucherService.getFunctionData(any(RequestInfo.class), any(String.class), any(Long.class)))
                .thenReturn(functionResponse);
        when(voucherService.getFundData(any(RequestInfo.class), any(String.class), any(Long.class)))
                .thenReturn(fundResponse);
        when(voucherService.createVoucherRequest(any(Object.class), any(Fund.class), any(Long.class), any(List.class),
                any(RequestInfo.class), any(String.class))).thenReturn(voucherRequest);
        revaluationService.createVoucherForRevaluation(revaluationRequest, headers);
    }

    @Test
    public void testSearch() {
        final List<Revaluation> revaluation = new ArrayList<>();
        revaluation.add(getRevaluationForSearch());

        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setRevaluations(revaluation);

        when(revaluationRepository.search(any(RevaluationCriteria.class))).thenReturn(revaluation);
        final RevaluationResponse expectedRevaluationResponse = revaluationService
                .search(any(RevaluationCriteria.class));

        assertEquals(revaluationResponse.toString(), expectedRevaluationResponse.toString());
    }

    private VoucherRequest getVoucherRequest() {
        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<>();
        final Voucher voucher = getVoucher();
        vouchers.add(voucher);
        voucherRequest.setRequestInfo(null);
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    private Voucher getVoucher() {
        final Voucher voucher = new Voucher();
        voucher.setCgvn(null);
        voucher.setDepartment(Long.valueOf("5"));
        voucher.setDescription("Asset Revaluation Jouranl Voucher");
        voucher.setFiscalPeriod(null);
        voucher.setFund(getFund());
        voucher.setId(Long.valueOf("50"));
        voucher.setLedgers(getLedgers());
        voucher.setModuleId(null);
        voucher.setName("Asset Revaluation");
        voucher.setOriginalVhId(null);
        voucher.setRefVhId(null);
        voucher.setStatus(Status.CREATED.toString());
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherNumber("1/GJV/00000197/08/2017-18");
        return null;
    }

    private List<VouchercreateAccountCodeDetails> getLedgers() {
        final List<VouchercreateAccountCodeDetails> ledgers = new ArrayList<>();
        final VouchercreateAccountCodeDetails creditAccountDetails = new VouchercreateAccountCodeDetails();
        creditAccountDetails.setCreditAmount(new BigDecimal("500"));
        creditAccountDetails.setDebitAmount(BigDecimal.ZERO);
        creditAccountDetails.setFunction(getFunction());
        creditAccountDetails.setGlcode("144005");
        creditAccountDetails.setSubledgerDetails(null);
        final VouchercreateAccountCodeDetails debitAccountDetails = new VouchercreateAccountCodeDetails();
        debitAccountDetails.setCreditAmount(BigDecimal.ZERO);
        debitAccountDetails.setDebitAmount(new BigDecimal("500"));
        debitAccountDetails.setFunction(getFunction());
        debitAccountDetails.setGlcode("122365");
        debitAccountDetails.setSubledgerDetails(null);
        ledgers.add(creditAccountDetails);
        ledgers.add(debitAccountDetails);
        return ledgers;
    }

    private FundResponse getFundResponse() {
        final FundResponse fundResponse = new FundResponse();
        final List<Fund> funds = new ArrayList<>();
        final Fund fund = getFund();
        funds.add(fund);
        fundResponse.setFunds(funds);
        fundResponse.setFund(null);
        fundResponse.setPage(null);
        fundResponse.setResponseInfo(null);
        return fundResponse;
    }

    private Fund getFund() {
        final Fund fund = new Fund();
        fund.setActive(true);
        fund.setCode("01");
        fund.setName("Municipal Fund");
        fund.setId(Long.valueOf("1"));
        fund.setIdentifier(null);
        fund.setLevel(null);
        fund.setParentId(null);
        return fund;
    }

    private FunctionResponse getFunctionResponse() {
        final FunctionResponse functionResponse = new FunctionResponse();
        final List<Function> functions = new ArrayList<>();
        final Function function = getFunction();
        functions.add(function);
        functionResponse.setFunctions(functions);
        functionResponse.setFunction(null);
        functionResponse.setPage(null);
        functionResponse.setResponseInfo(null);
        return functionResponse;
    }

    private Function getFunction() {
        final Function function = new Function();
        function.setId(Long.valueOf("5"));
        function.setCode("0600");
        function.setActive(true);
        function.setName("Estate");
        function.setParentId(null);
        function.setLevel(null);
        return function;
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setId(Long.valueOf("1"));
        asset.setName("asset name");
        asset.setStatus(Status.CREATED.toString());
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
        asset.setEnableYearWiseDepreciation(true);

        final Location location = new Location();
        location.setLocality(4l);
        location.setDoorNo("door no");

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(1l);
        assetCategory.setName("category name");

        asset.setLocationDetails(location);
        asset.setAssetCategory(assetCategory);

        final Department department = new Department();
        department.setId(Long.valueOf("5"));
        department.setCode("ENG");
        department.setName("ENGINEERING");

        asset.setDepartment(department);
        return asset;
    }

    private HttpHeaders getHttpHeaders() {
        final List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.COOKIE, "SESSIONID=123");
        requestHeaders.setPragma("no-cache");
        requestHeaders.setConnection("keep-alive");
        requestHeaders.setCacheControl("no-cache");
        requestHeaders.setAccept(mediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    private RevaluationRequest getRevaluationRequest() {
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRequestInfo(new RequestInfo());
        revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
        return revaluationRequest;
    }

    private RevaluationResponse getRevaluationResponse() {
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        final List<Revaluation> revaluations = new ArrayList<>();
        revaluations.add(getRevaluationForCreateAsync());
        revaluationResponse.setResposneInfo(null);
        revaluationResponse.setRevaluations(revaluations);
        return revaluationResponse;
    }

    private Revaluation getRevaluationForCreateAsync() {

        final Revaluation revaluation = new Revaluation();
        revaluation.setId(Long.valueOf("0"));
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("31"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.68"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10.0"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("2"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setScheme(Long.valueOf("4"));
        revaluation.setSubScheme(Long.valueOf("5"));
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());
        revaluation.setAuditDetails(getAuditDetails());
        revaluation.setVoucherReference("42");
        return revaluation;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("5");
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy("5");
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

    private Revaluation getRevaluationForSearch() {

        final Revaluation revaluation = new Revaluation();
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("31"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.68"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("2"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setScheme(Long.valueOf("4"));
        revaluation.setSubScheme(Long.valueOf("5"));
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());
        revaluation.setAuditDetails(getAuditDetails());

        return revaluation;
    }

    private RevaluationResponse getRevaluation(final String filePath) throws IOException {
        final String empJson = new FileUtils().getFileContents(filePath);
        return new ObjectMapper().readValue(empJson, RevaluationResponse.class);
    }
}
