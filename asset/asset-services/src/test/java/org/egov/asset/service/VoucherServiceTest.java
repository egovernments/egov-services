/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.FundResponse;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.contract.VoucherResponse;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountContract;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.Department;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.FiscalPeriod;
import org.egov.asset.model.Function;
import org.egov.asset.model.Functionary;
import org.egov.asset.model.Fund;
import org.egov.asset.model.FundSource;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.Scheme;
import org.egov.asset.model.SubScheme;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetFinancialParams;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class VoucherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private VoucherAccountCodeDetails VoucherAccountCodeDetails;

    @InjectMocks
    private VoucherService voucherService;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Test
    public void test_shuould_create_VoucherRequest_For_Revalaution()
            throws JsonParseException, JsonMappingException, IOException {

        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        revaluationRequest.getRequestInfo();
        final Revaluation revaluation = revaluationRequest.getRevaluation();

        final Asset asset = get_Asset();
        final List<VoucherAccountCodeDetails> accountCodeDetails = getLedgers();

        final String tenantId = revaluation.getTenantId();

        final Map<String, String> voucherParams = getAssetFinancialParamsMap();

        when(mapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(voucherParams);

        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId))
                        .thenReturn("Asset Revaluation");
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId))
                        .thenReturn("Asset Revaluation Journal Voucher");
        final VoucherRequest generatedVoucherRequest = voucherService.createRevaluationVoucherRequest(revaluation,
                accountCodeDetails, asset.getId(), asset.getDepartment().getId(), getHttpHeaders());

        final Fund fund = new Fund();
        fund.setId(Long.valueOf("1"));

        final Voucher voucher = getVoucher(asset.getDepartment().getId(), fund);

        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId));

        voucher.setSource(
                null + "/asset-web/app/asset/create-asset-revaluation.html?id=" + asset.getId() + "&type=view");

        final Function function = new Function();
        function.setId(Long.valueOf("124"));

        final List<VoucherAccountCodeDetails> voucherAccountCodeDetails = voucher.getLedgers();
        for (final VoucherAccountCodeDetails acd : voucherAccountCodeDetails)
            acd.setFunction(function);

        voucher.setLedgers(voucherAccountCodeDetails);

        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest expectedVoucherRequest = new VoucherRequest();
        expectedVoucherRequest.setRequestInfo(new org.egov.asset.model.RequestInfo());
        expectedVoucherRequest.setVouchers(vouchers);

        System.out.println(generatedVoucherRequest + "\n");
        System.err.println(expectedVoucherRequest);

        assertEquals(expectedVoucherRequest.getVouchers().toString(), generatedVoucherRequest.getVouchers().toString());

    }

    @Test
    public void test_should_create_VoucherRequest_For_Disposal()
            throws JsonParseException, JsonMappingException, IOException {

        final DisposalRequest disposalRequest = getDisposalRequest();
        disposalRequest.getRequestInfo();
        final Disposal disposal = disposalRequest.getDisposal();

        final Asset asset = get_Asset();
        final List<VoucherAccountCodeDetails> accountCodeDetails = getLedgers();

        final String tenantId = disposal.getTenantId();

        final Map<String, String> voucherParams = getAssetFinancialParamsMap();

        when(mapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(voucherParams);
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERNAME,
                tenantId)).thenReturn("Asset Disposal");
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERDESCRIPTION, tenantId))
                        .thenReturn("Asset Disposal Journal Voucher");

        final VoucherRequest generatedVoucherRequest = voucherService.createDisposalVoucherRequest(disposal,
                asset.getId(), asset.getDepartment().getId(), accountCodeDetails,asset.getFunction(), getHttpHeaders());

        final Fund fund = getFund();
        final Voucher voucher = getVoucher(asset.getDepartment().getId(), fund);

        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERDESCRIPTION, tenantId));

        voucher.setSource(null + "/asset-web/app/asset/create-asset-sale.html?id=" + asset.getId() + "&type=view");
        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest expectedVoucherRequest = new VoucherRequest();
        expectedVoucherRequest.setRequestInfo(new org.egov.asset.model.RequestInfo());
        expectedVoucherRequest.setVouchers(vouchers);

        System.out.println(generatedVoucherRequest + "\n");
        System.err.println(expectedVoucherRequest);

        assertEquals(expectedVoucherRequest.getVouchers().toString(), generatedVoucherRequest.getVouchers().toString());

    }

    private Map<String, String> getAssetFinancialParamsMap() {
        final Map<String, String> voucherParams = new HashMap<String, String>();
        voucherParams.put(AssetFinancialParams.FUND.toString(), "01");
        voucherParams.put(AssetFinancialParams.FUNCTION.toString(), "0600");
        voucherParams.put(AssetFinancialParams.FUNCTIONARY.toString(), "01");
        voucherParams.put(AssetFinancialParams.SCHEME.toString(), "01");
        voucherParams.put(AssetFinancialParams.SUBSCHEME.toString(), "01");
        voucherParams.put(AssetFinancialParams.FISCAL.toString(), "01");
        voucherParams.put(AssetFinancialParams.FUNDSOURCE.toString(), "01");
        return voucherParams;
    }

   /* @Test
    public void test_shuould_create_VoucherRequest_For_Depreciation()
            throws JsonParseException, JsonMappingException, IOException {

        final DepreciationRequest depreciationRequest = DepreciationRequest.builder().requestInfo(new RequestInfo())
                .depreciationCriteria(getDepreciationCriteria()).build();
        depreciationRequest.getRequestInfo();
        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();

        final Asset asset = get_Asset();
        final List<VoucherAccountCodeDetails> accountCodeDetails = getLedgers();

        final String tenantId = depreciationCriteria.getTenantId();

        final List<CalculationAssetDetails> calculationAssetDetailList = getCalculationAssetDetailList();

        final Map<String, String> voucherParams = getAssetFinancialParamsMap();

        when(mapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(voucherParams);
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERNAME, tenantId))
                        .thenReturn("Asset Disposal");
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERDESCRIPTION, tenantId))
                        .thenReturn("Asset Disposal Journal Voucher");
        final VoucherRequest generatedVoucherRequest = voucherService.createDepreciationVoucherRequest(
                calculationAssetDetailList, asset.getDepartment().getId(), accountCodeDetails, tenantId,
                getHttpHeaders());

        final Fund fund = getFund();
        final Voucher voucher = getVoucher(asset.getDepartment().getId(), fund);
        final List<Voucher> vouchers = new ArrayList<>();

        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERDESCRIPTION, tenantId));

        vouchers.add(voucher);

        final VoucherRequest expectedVoucherRequest = new VoucherRequest();
        expectedVoucherRequest.setRequestInfo(new org.egov.asset.model.RequestInfo());
        expectedVoucherRequest.setVouchers(vouchers);

        System.out.println(generatedVoucherRequest + "\n");
        System.err.println(expectedVoucherRequest);

        assertEquals(expectedVoucherRequest.getVouchers().toString(), generatedVoucherRequest.getVouchers().toString());

    }
*/
    private List<CalculationAssetDetails> getCalculationAssetDetailList() {
        final List<CalculationAssetDetails> calculationAssetDetails = new ArrayList<CalculationAssetDetails>();
        final CalculationAssetDetails calculationAssetDetail = new CalculationAssetDetails();
        calculationAssetDetail.setAccumulatedDepreciation(BigDecimal.ZERO);
        calculationAssetDetail.setAccumulatedDepreciationAccount(Long.valueOf("1947"));
        calculationAssetDetail.setAssetCategoryDepreciationRate(null);
        calculationAssetDetail.setAssetCategoryId(Long.valueOf("196"));
        calculationAssetDetail.setAssetCategoryName("Kalyana Mandapam");
        calculationAssetDetail.setAssetDepreciationRate(Double.valueOf("16.53"));
        calculationAssetDetail.setAssetId(Long.valueOf("597"));
        calculationAssetDetail.setAssetReference(null);
        calculationAssetDetail.setDepartmentId(Long.valueOf("5"));
        calculationAssetDetail.setDepreciationExpenseAccount(Long.valueOf("1906"));
        calculationAssetDetail.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        calculationAssetDetail.setEnableYearWiseDepreciation(true);
        calculationAssetDetail.setFinancialyear("2017-18");
        calculationAssetDetail.setGrossValue(new BigDecimal("15000"));
        calculationAssetDetail.setYearwisedepreciationrate(Double.valueOf("15"));
        calculationAssetDetails.add(calculationAssetDetail);
        return calculationAssetDetails;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setAssetIds(null);
        depreciationCriteria.setFinancialYear("2017");
        depreciationCriteria.setFromDate(Long.valueOf(0));
        depreciationCriteria.setToDate(Long.valueOf("0"));
        depreciationCriteria.setTenantId("ap.kurnool");
        return depreciationCriteria;
    }

    private DisposalRequest getDisposalRequest() {
        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setRequestInfo(new RequestInfo());
        disposalRequest.setDisposal(getDisposal());
        return disposalRequest;
    }

    private Disposal getDisposal() {
        final Disposal disposal = new Disposal();
        disposal.setId(Long.valueOf("15"));
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
        disposal.setProfitLossVoucherReference("6");
        disposal.setAuditDetails(getAuditDetails());
        disposal.setProfitLossVoucherReference("6");

        return disposal;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createVoucherTest() throws JsonParseException, JsonMappingException, IOException, JSONException {
        final VoucherRequest voucherRequest = getVoucherRequest();
        final HttpHeaders headers = getHttpHeaders();
        final VoucherResponse voucherResponse = getVoucherResponse();
        final ResponseEntity<JSONObject> responseEntity = getResponseEntity();
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);
        when(mapper.readValue(any(String.class), any(Class.class))).thenReturn(voucherResponse);
        voucherService.createVoucher(voucherRequest, "ap.kurnool", headers);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getGlCodesTest() {
        final ChartOfAccountContractResponse chartOfAccountContractResponse = getChartOfAccountContractResponse();
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(chartOfAccountContractResponse);
        voucherService.getGlCodes(new RequestInfo(), "ap.kurnool", Long.valueOf("1224501"), new BigDecimal("5000"),
                true, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getFundFromVoucherMapTest() throws JsonParseException, JsonMappingException, IOException {
        final FundResponse fundResponse = getFundResponse();
        final Map<String, String> voucherParams = getVoucherParamsMap();
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(any(AssetConfigurationKeys.class),
                any(String.class))).thenReturn("{\"Fund\":\"01\",\"Function\":\"0600\"}");
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(fundResponse);
        when(mapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(voucherParams);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getFuntionFromVoucherMapTest() throws JsonParseException, JsonMappingException, IOException {
        final FunctionResponse functionResponse = getFunctionResponse();
        final Map<String, String> voucherParams = getVoucherParamsMap();
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(any(AssetConfigurationKeys.class),
                any(String.class))).thenReturn("{\"Fund\":\"01\",\"Function\":\"0600\"}");
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(functionResponse);
        when(mapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(voucherParams);
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
        function.setCode("0600");
        return function;
    }

    private HashMap<String, String> getVoucherParamsMap() {
        final HashMap<String, String> voucherParamsMap = new HashMap<>();
        voucherParamsMap.put(AssetFinancialParams.FUND.toString(), "01");
        voucherParamsMap.put(AssetFinancialParams.FUNCTION.toString(), "0600");
        return voucherParamsMap;
    }

    private FundResponse getFundResponse() {
        final FundResponse fundResponse = new FundResponse();
        final List<Fund> funds = new ArrayList<>();
        funds.add(getFund());
        fundResponse.setFunds(funds);
        fundResponse.setFund(null);
        fundResponse.setPage(null);
        fundResponse.setResponseInfo(null);
        return fundResponse;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getSubledgerDetailsTest() {
        final ChartOfAccountDetailContractResponse chartOfAccountContractResponse = getChartOfAccountDetailContractResponse();
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(chartOfAccountContractResponse);
        voucherService.getSubledgerDetails(new RequestInfo(), "ap.kurnool", Long.valueOf("1224501"));
    }

    private ChartOfAccountDetailContractResponse getChartOfAccountDetailContractResponse() {
        final ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
        final List<ChartOfAccountDetailContract> chartOfAccountDetails = new ArrayList<>();
        final ChartOfAccountDetailContract chartOfAccountDetail = new ChartOfAccountDetailContract();
        chartOfAccountDetail.setAccountDetailType(null);
        chartOfAccountDetail.setChartOfAccount(getChartOfAccount());
        chartOfAccountDetail.setAuditDetails(getAuditDetails());
        chartOfAccountDetails.add(chartOfAccountDetail);
        chartOfAccountDetailContractResponse.setChartOfAccountDetails(chartOfAccountDetails);
        chartOfAccountDetailContractResponse.setChartOfAccountDetail(null);
        chartOfAccountDetailContractResponse.setPage(null);
        chartOfAccountDetailContractResponse.setResponseInfo(null);
        return chartOfAccountDetailContractResponse;
    }

    private ChartOfAccountContractResponse getChartOfAccountContractResponse() {
        final ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        final List<ChartOfAccountContract> chartOfAccounts = new ArrayList<>();
        final ChartOfAccountContract chartOfAccount = getChartOfAccount();
        chartOfAccounts.add(chartOfAccount);
        chartOfAccountContractResponse.setChartOfAccounts(chartOfAccounts);
        chartOfAccountContractResponse.setChartOfAccount(null);
        chartOfAccountContractResponse.setResponseInfo(null);
        return chartOfAccountContractResponse;
    }

    private ChartOfAccountContract getChartOfAccount() {
        final ChartOfAccountContract chartOfAccount = new ChartOfAccountContract();
        chartOfAccount.setGlcode("1224501");
        chartOfAccount.setBudgetCheckRequired(false);
        chartOfAccount.setClassification(Long.valueOf("4"));
        chartOfAccount.setDescription("PNB Account");
        chartOfAccount.setFunctionRequired(false);
        chartOfAccount.setMajorCode("1224501");
        chartOfAccount.setIsActiveForPosting(true);
        return chartOfAccount;
    }

    private ResponseEntity<JSONObject> getResponseEntity() throws JSONException {
        final JSONObject jsonObj = new JSONObject(
                "{\"page\":null,\"Vouchers\":[{\"id\":120675,\"name\":\"voucherName1\",\"type\":\"JOURNAL VOUCHER\",\"voucherNumber\":\"1/GJV/00000217/06/2017-18\",\"description\":\"Create Voucher\",\"voucherDate\":\"16-06-2017\",\"fund\":{\"version\":0,\"id\":1,\"name\":\"Municipal Fund\",\"code\":\"01\",\"identifier\":\"1\",\"llevel\":0,\"parentId\":null,\"isnotleaf\":false,\"isactive\":true,\"createdby\":null,\"createdDate\":\"11-02-2016\",\"lastModifiedBy\":null,\"lastModifiedDate\":null,\"new\":false},\"fiscalPeriod\":{\"id\":51,\"name\":\"201718\",\"financialYear\":{\"id\":51,\"finYearRange\":\"2017-18\",\"startingDate\":null,\"endingDate\":\"01-04-2017\",\"active\":true,\"isActiveForPosting\":true,\"isClosed\":false,\"transferClosingBalance\":false},\"startingDate\":\"01-04-2017\",\"endingDate\":\"31-03-2018\",\"active\":true,\"isActiveForPosting\":false,\"isClosed\":null},\"status\":\"Approved\",\"originalVhId\":null,\"refVhId\":null,\"cgvn\":\"1/JVG/CGVN0000000807\",\"moduleId\":null,\"ledgers\":[{\"id\":400298,\"orderId\":null,\"glcode\":\"3501001\",\"debitAmount\":0.0,\"creditAmount\":1.0,\"function\":{\"id\":111,\"name\":\"Public Health\",\"code\":\"3100\",\"level\":2,\"active\":true,\"isParent\":true,\"parentId\":32},\"subledgerDetails\":[{\"id\":17759,\"accountDetailType\":{\"id\":4,\"name\":\"contractor\",\"description\":\"contractor\",\"tableName\":\"egw_contractor\",\"active\":true,\"fullyQualifiedName\":\"org.egov.works.models.masters.Contractor\"},\"accountDetailKey\":{\"id\":1,\"accountDetailType\":{\"id\":4,\"name\":\"contractor\",\"description\":\"contractor\",\"tableName\":\"egw_contractor\",\"active\":true,\"fullyQualifiedName\":\"org.egov.works.models.masters.Contractor\"},\"key\":null},\"amount\":1.0}]},{\"id\":400297,\"orderId\":null,\"glcode\":\"1100101\",\"debitAmount\":1.0,\"creditAmount\":0.0,\"function\":{\"id\":111,\"name\":\"Public Health\",\"code\":\"3100\",\"level\":2,\"active\":true,\"isParent\":true,\"parentId\":32},\"subledgerDetails\":[]}]}],\"ResponseInfo\":null}");
        final ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(jsonObj, HttpStatus.CREATED);
        return responseEntity;
    }

    private VoucherResponse getVoucherResponse() {
        final VoucherResponse voucherResponse = new VoucherResponse();
        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(getVoucher(Long.valueOf("5"), getFund()));
        voucherResponse.setPage(null);
        voucherResponse.setVouchers(vouchers);
        voucherResponse.setResponseInfo(null);
        return voucherResponse;
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

    private VoucherRequest getVoucherRequest() {
        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<Voucher>();
        vouchers.add(getVoucher(Long.valueOf("5"), getFund()));
        voucherRequest.setRequestInfo(new org.egov.asset.model.RequestInfo());
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    private Fund getFund() {
        final Fund fund = new Fund();
        fund.setCode("01");
        return fund;
    }

    private Voucher getVoucher(final Long departmentId, final Fund fund) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(getLedgers());
        voucher.setDepartment(departmentId);
        voucher.setFund(fund);

        final FiscalPeriod fiscalPeriod = new FiscalPeriod();
        fiscalPeriod.setName("01");
        voucher.setFiscalPeriod(fiscalPeriod);

        final Scheme scheme = new Scheme();
        scheme.setCode("01");
        voucher.setScheme(scheme);

        final SubScheme subScheme = new SubScheme();
        subScheme.setCode("01");
        voucher.setSubScheme(subScheme);

        final Functionary functionary = new Functionary();
        functionary.setCode("01");
        voucher.setFunctionary(functionary);

        final FundSource fundSource = new FundSource();
        fundSource.setCode("01");
        voucher.setFundsource(fundSource);

        return voucher;
    }

    private List<VoucherAccountCodeDetails> getLedgers() {
        final List<VoucherAccountCodeDetails> ledgers = new ArrayList<>();
        final VoucherAccountCodeDetails creditAccountDetails = new VoucherAccountCodeDetails();
        creditAccountDetails.setCreditAmount(new BigDecimal("500"));
        creditAccountDetails.setDebitAmount(BigDecimal.ZERO);
        creditAccountDetails.setFunction(getFunction());
        creditAccountDetails.setGlcode("144005");
        creditAccountDetails.setSubledgerDetails(null);
        final VoucherAccountCodeDetails debitAccountDetails = new VoucherAccountCodeDetails();
        debitAccountDetails.setCreditAmount(BigDecimal.ZERO);
        debitAccountDetails.setDebitAmount(new BigDecimal("500"));
        debitAccountDetails.setFunction(getFunction());
        debitAccountDetails.setGlcode("122365");
        debitAccountDetails.setSubledgerDetails(null);
        ledgers.add(creditAccountDetails);
        ledgers.add(debitAccountDetails);
        return ledgers;
    }

    private Asset get_Asset() {
        final Asset asset = new Asset();
        asset.setId(Long.valueOf("12"));
        final Department department = new Department();
        department.setId(Long.valueOf("2"));
        asset.setDepartment(department);
        return asset;
    }

    private RevaluationRequest getRevaluationRequest() {
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRequestInfo(new RequestInfo());
        final Revaluation revaluation = new Revaluation();
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("12"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.58"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.INCREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("124"));
        revaluation.setFund(Long.valueOf("1"));
        revaluation.setScheme(Long.valueOf("4"));
        revaluation.setSubScheme(Long.valueOf("5"));
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());

        final AuditDetails auditDetails = getAuditDetails();
        revaluation.setAuditDetails(auditDetails);

        revaluationRequest.setRevaluation(revaluation);
        return revaluationRequest;

    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("5");
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy("5");
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

}
