package org.egov.asset.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.FundResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.contract.VoucherResponse;
import org.egov.asset.model.ChartOfAccountContract;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetFinancialParams;
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VoucherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String createVoucher(final VoucherRequest voucherRequest, final String tenantId, final HttpHeaders headers) {
        headers.setOrigin("http://kurnool-pilot-services.egovernments.org");
        final String createVoucherUrl = headers.getOrigin() + applicationProperties.getEgfServiceVoucherCreatePath()
                + "?tenantId=" + tenantId;
        log.debug("Voucher API Request URL :: " + createVoucherUrl);
        log.debug("VoucherRequest :: " + voucherRequest);

        final List<String> cookies = headers.get("cookie");
        log.debug("cookies::" + cookies);

        final List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);

        final String cookie = cookies.stream().collect(Collectors.joining(";"));
        log.debug("Cookie for voucher request header :: " + cookie);

        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.COOKIE, cookie);
        requestHeaders.setPragma("no-cache");
        requestHeaders.setConnection("keep-alive");
        requestHeaders.setCacheControl("no-cache");
        requestHeaders.setAccept(mediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccessControlAllowOrigin(headers.getOrigin());

        log.debug("Request Headers for Voucher Request :: " + requestHeaders);

        voucherRequest.getRequestInfo().setTs(null);

        final HttpEntity requestEntity = new HttpEntity(voucherRequest, requestHeaders);
        log.debug("Request Entity ::" + requestEntity);
        final ResponseEntity<JSONObject> response = restTemplate.exchange(createVoucherUrl, HttpMethod.POST,
                requestEntity, JSONObject.class);
        log.debug("Response From Voucher API :: " + response);
        final JSONObject voucherResponse = response.getBody();
        log.debug("VoucherResponse :: " + voucherResponse);
        try {
            final VoucherResponse voucherRes = mapper.readValue(voucherResponse.toString(), VoucherResponse.class);
            final String voucherNumber = voucherRes.getVouchers().get(0).getVoucherNumber();
            log.debug("Voucher Number is :: " + voucherNumber);
            return voucherNumber;
        } catch (final IOException e) {
            throw new RuntimeException("Voucher response Deserialization Issue :: " + e.getMessage());
        }
    }

    public VoucherRequest createVoucherRequest(final Object entity, final Fund fund, final Long depratmentId,
            final List<VouchercreateAccountCodeDetails> accountCodeDetails, final RequestInfo requestInfo,
            final String tenantId) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(accountCodeDetails);
        voucher.setDepartment(depratmentId);
        voucher.setFund(fund);

        log.debug("Entity :: " + entity);

        if (entity instanceof Revaluation) {
            log.info("Setting Revaluation Voucher Name and Description ");
            voucher.setName(assetConfigurationService
                    .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId));
            voucher.setDescription(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(
                    AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId));
        } else if (entity instanceof Disposal) {
            log.info("Setting Disposal Voucher Name and Description ");
            voucher.setName(assetConfigurationService
                    .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERNAME, tenantId));
            voucher.setDescription(assetConfigurationService
                    .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERDESCRIPTION, tenantId));
        } else {
            log.info("Setting Depreciation Voucher Name and Description ");
            voucher.setName(assetConfigurationService
                    .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERNAME, tenantId));
            voucher.setDescription(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(
                    AssetConfigurationKeys.DEPRECIATIONVOUCHERDESCRIPTION, tenantId));
        }
        log.debug("Voucher :: " + voucher);
        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setRequestInfo(requestInfo);
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    public VouchercreateAccountCodeDetails getGlCodes(final RequestInfo requestInfo, final String tenantId,
            final Long accountId, final BigDecimal amount, final Function function, final Boolean iscredit,
            final Boolean isDebit) {

        final VouchercreateAccountCodeDetails debitAccountCodeDetail = new VouchercreateAccountCodeDetails();
        final List<ChartOfAccountContract> chartOfAccounts = getChartOfAccounts(requestInfo, tenantId, accountId);

        if (!chartOfAccounts.isEmpty()) {
            final ChartOfAccountContract chartOfAccount = chartOfAccounts.get(0);
            log.debug("Chart Of Account : " + chartOfAccount.getName() + chartOfAccount.getGlcode());
            if (!chartOfAccount.getIsActiveForPosting())
                throw new RuntimeException(
                        "Chart of Account " + chartOfAccount.getName() + " is not active for posting");
            else
                debitAccountCodeDetail.setGlcode(chartOfAccount.getGlcode());
        } else
            throw new RuntimeException("Chart of Account is not present for account : " + accountId);
        if (iscredit)
            debitAccountCodeDetail.setCreditAmount(amount);
        if (isDebit)
            debitAccountCodeDetail.setDebitAmount(amount);

        debitAccountCodeDetail.setFunction(function);
        log.debug("Account Code Detail :: " + debitAccountCodeDetail);

        return debitAccountCodeDetail;
    }

    public List<ChartOfAccountContract> getChartOfAccounts(final RequestInfo requestInfo, final String tenantId,
            final Long accountId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceChartOfAccountsSearchPath() + "?tenantId=" + tenantId + "&id="
                + accountId;
        log.debug("Chart of Account URL ::" + url);
        log.debug("Chart of Account Request Info :: " + requestInfo);
        final ChartOfAccountContractResponse chartOfAccountContractResponse = restTemplate.postForObject(url,
                requestInfo, ChartOfAccountContractResponse.class);
        log.debug("Chart of Account Response :: " + chartOfAccountContractResponse);

        final List<ChartOfAccountContract> chartOfAccounts = chartOfAccountContractResponse.getChartOfAccounts();
        return chartOfAccounts;
    }

    public List<ChartOfAccountDetailContract> getSubledgerDetails(final RequestInfo requestInfo, final String tenantId,
            final Long accountId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceChartOfAccountsDetailsSearchPath() + "?tenantId=" + tenantId
                + "&id=" + accountId;
        log.debug("subledger details check URL :: " + url);
        log.debug("subledger details request info :: " + requestInfo);
        final ChartOfAccountDetailContractResponse coAccountDetailContractResponse = restTemplate.postForObject(url,
                requestInfo, ChartOfAccountDetailContractResponse.class);
        log.debug("subledger details response :: " + coAccountDetailContractResponse);
        return coAccountDetailContractResponse.getChartOfAccountDetails();
    }

    public void validateSubLedgerDetails(final List<ChartOfAccountDetailContract> creditableCOA,
            final List<ChartOfAccountDetailContract> debitableCOA) {
        log.debug("Validating Sub Ledger Details for Chart of Accounts ");
        if (creditableCOA != null && debitableCOA != null && !creditableCOA.isEmpty() && !debitableCOA.isEmpty())
            throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");
    }

    public FundResponse getFundData(final RequestInfo requestInfo, final String tenantId, final Long fundId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + tenantId + "&id=" + fundId;
        log.debug("fund search url :: " + url);
        final FundResponse fundResponse = restTemplate.postForObject(url, requestInfo, FundResponse.class);
        log.debug("fund Response :: " + fundResponse);
        return fundResponse;
    }

    public FunctionResponse getFunctionData(final RequestInfo requestInfo, final String tenantId,
            final Long functionId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + tenantId + "&id="
                + functionId;
        log.debug("function search url :: " + url);
        final FunctionResponse functionResponse = restTemplate.postForObject(url, requestInfo, FunctionResponse.class);
        log.debug("function Response :: " + functionResponse);
        return functionResponse;
    }

    public Fund getFundFromVoucherMap(final RequestInfo requestInfo, final String tenantId) {
        Map<String, String> voucherParamsMap = null;
        try {
            voucherParamsMap = getVoucherParamsMap(tenantId);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final String fundCode = voucherParamsMap.get(AssetFinancialParams.FUND.toString());
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + tenantId + "&code=" + fundCode;
        log.debug("fund search url :: " + url);
        final FundResponse fundResponse = restTemplate.postForObject(url, requestInfo, FundResponse.class);
        log.debug("fund Response :: " + fundResponse);
        final List<Fund> funds = fundResponse.getFunds();
        if (funds.isEmpty())
            throw new RuntimeException("Fund Doesn't exists for code :: " + fundCode);
        else
            return funds.get(0);
        
    }

    public Function getFunctionFromVoucherMap(final RequestInfo requestInfo, final String tenantId) {
        Map<String, String> voucherParamsMap = null;
        try {
            voucherParamsMap = getVoucherParamsMap(tenantId);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final String functionCode = voucherParamsMap.get(AssetFinancialParams.FUNCTION.toString());
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + tenantId + "&code="
                + functionCode;
        log.debug("function search url :: " + url);
        final FunctionResponse functionResponse = restTemplate.postForObject(url, requestInfo, FunctionResponse.class);
        log.debug("function Response :: " + functionResponse);
        final List<Function> functions = functionResponse.getFunctions();
        if (functions.isEmpty())
            throw new RuntimeException("Function Doesn't exists for code :: " + functionCode);
        else
            return functions.get(0);
    }

    public HashMap<String, String> getVoucherParamsMap(final String tenantId)
            throws IOException, JsonParseException, JsonMappingException {
        final String voucherParams = assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.VOUCHERPARAMS, tenantId);

        log.debug("Voucher Parameters :: " + voucherParams);
        final TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };

        return mapper.readValue(voucherParams, typeRef);
    }

}
