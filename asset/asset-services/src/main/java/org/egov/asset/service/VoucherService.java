package org.egov.asset.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
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
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
    public Long createVoucher(final VoucherRequest voucherRequest, final String tenantId, final HttpHeaders headers) {
        final String createVoucherUrl = headers.getOrigin() + applicationProperties.getEgfServiceVoucherCreatePath()
                + "?tenantId=" + tenantId;
        log.debug("Voucher API Request URL :: " + createVoucherUrl);
        log.debug("VoucherRequest :: " + voucherRequest);
        final Error err = new Error();
        VoucherResponse voucherRes = new VoucherResponse();

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
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        try {
            response = restTemplate.exchange(createVoucherUrl, HttpMethod.POST, requestEntity, String.class);
            log.debug("VoucherResponse :: " + response.getBody());
        } catch (final HttpClientErrorException e) {
            throw new RuntimeException("Voucher can not be created because :: " + err.getMessage());
        }
        try {
            voucherRes = mapper.readValue(response.getBody(), VoucherResponse.class);
            final Long voucherId = voucherRes.getVouchers().get(0).getId();
            log.debug("Voucher Id is :: " + voucherId);
            return voucherId;
        } catch (final IOException e) {
            throw new RuntimeException("Voucher response Deserialization Issue :: " + e.getMessage());
        }
    }

    public VoucherRequest createVoucherRequest(final Object entity, final Long fundId, final Long depratmentId,
            final List<VouchercreateAccountCodeDetails> accountCodeDetails, final RequestInfo requestInfo,
            final String tenantId) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final Fund fund = new Fund();
        fund.setId(fundId);

        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(accountCodeDetails);
        voucher.setDepartment(depratmentId);

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
        }
        voucher.setFund(fund);

        log.debug("Voucher :: " + voucher);
        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setRequestInfo(requestInfo);
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    public VouchercreateAccountCodeDetails getGlCodes(final RequestInfo requestInfo, final String tenantId,
            final Long accountId, final BigDecimal amount, final Long functionId, final Boolean iscredit,
            final Boolean isDebit) {

        final VouchercreateAccountCodeDetails debitAccountCodeDetail = new VouchercreateAccountCodeDetails();
        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceChartOfAccountsSearchPath() + "?tenantId=" + tenantId + "&id="
                + accountId;
        log.debug("Chart of Account URL ::" + url);
        log.debug("Chart of Account Request Info :: " + requestInfo);
        chartOfAccountContractResponse = restTemplate.postForObject(url, requestInfo,
                ChartOfAccountContractResponse.class);
        log.debug("Chart of Account Response :: " + chartOfAccountContractResponse);

        final List<ChartOfAccountContract> chartOfAccounts = chartOfAccountContractResponse.getChartOfAccounts();

        if (!chartOfAccounts.isEmpty()) {
            final ChartOfAccountContract chartOfAccount = chartOfAccounts.get(0);
            log.debug("Chart Of Account : " + chartOfAccount);
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

        final Function function = new Function();
        function.setId(functionId);
        debitAccountCodeDetail.setFunction(function);
        log.debug("Account Code Detail :: " + debitAccountCodeDetail);

        return debitAccountCodeDetail;
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
}
