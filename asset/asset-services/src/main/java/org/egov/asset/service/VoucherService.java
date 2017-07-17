package org.egov.asset.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.contract.VoucherResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.ChartOfAccountContract;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    public VoucherRequest createVoucherRequestForReevalaution(final RevaluationRequest revaluationRequest,
            final Asset asset, final List<VouchercreateAccountCodeDetails> accountCodeDetails) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final Fund fund = new Fund();
        fund.setId(revaluationRequest.getRevaluation().getFund());

        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(accountCodeDetails);
        voucher.setDepartment(asset.getDepartment().getId());
        voucher.setName(applicationProperties.getReevaluationVoucherName());
        voucher.setDescription(applicationProperties.getReevaluationVoucherDescription());
        voucher.setFund(fund);

        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setRequestInfo(new RequestInfo());
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    public Long createVoucher(final VoucherRequest voucherRequest, final String tenantId) {
        final String createVoucherUrl = applicationProperties.getMunicipalityHostName()
                + applicationProperties.getEgfServiceVoucherCreatePath() + "?tenantId=" + tenantId;
        logger.debug("VoucherRequest : " + voucherRequest);
        Error err = new Error();
        VoucherResponse voucherRes = new VoucherResponse();
        try {
            final JSONObject voucherResponse = restTemplate.postForObject(createVoucherUrl, voucherRequest,
                    JSONObject.class);
            logger.debug("VoucherResponse : " + voucherResponse);
            try {
                voucherRes = mapper.readValue(voucherResponse.toString(), VoucherResponse.class);
            } catch (final IOException e) {
                logger.debug("Voucher response Deserialization Issue : " + e.getMessage());
            }
            return voucherRes.getVouchers().get(0).getId();
        } catch (final HttpClientErrorException e) {

            try {
                err = mapper.readValue(e.getResponseBodyAsString(), Error.class);
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException("Voucher can not be created because : " + err.getMessage());
        }
    }

    public List<ChartOfAccountDetailContract> getSubledgerDetails(final RequestInfo requestInfo, final String tenantId,
            final Long accountId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceChartOfAccountsDetailsSearchPath() + "?tenantId=" + tenantId
                + "&id=" + accountId;
        final ChartOfAccountDetailContractResponse coAccountDetailContractResponse = restTemplate.postForObject(url,
                requestInfo, ChartOfAccountDetailContractResponse.class);
        return coAccountDetailContractResponse.getChartOfAccountDetails();
    }

    public VouchercreateAccountCodeDetails getGlCodes(final RequestInfo requestInfo, final String tenantId,
            final Long accountId, final BigDecimal amount, final Long functionId, final Boolean iscredit,
            final Boolean isDebit) {

        final VouchercreateAccountCodeDetails debitAccountCodeDetail = new VouchercreateAccountCodeDetails();
        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceChartOfAccountsSearchPath() + "?tenantId=" + tenantId + "&id="
                + accountId;
        try {
            chartOfAccountContractResponse = restTemplate.postForObject(url, requestInfo,
                    ChartOfAccountContractResponse.class);

            final List<ChartOfAccountContract> chartOfAccounts = chartOfAccountContractResponse.getChartOfAccounts();

            if (!chartOfAccounts.isEmpty()) {
                final ChartOfAccountContract chartOfAccount = chartOfAccounts.get(0);
                logger.debug("Chart Of Account : " + chartOfAccount);
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

        } catch (final Exception ex) {
            logger.debug("Some problem occured while getting account details : ", ex);
            throw new RuntimeException(ex);
        }

        return debitAccountCodeDetail;
    }

    public VoucherRequest createVoucherRequestForDisposal(final DisposalRequest disposalRequest, final Asset asset,
            final List<VouchercreateAccountCodeDetails> accountCodeDetails) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final Fund fund = new Fund();
        fund.setId(disposalRequest.getDisposal().getFund());

        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(accountCodeDetails);
        voucher.setDepartment(asset.getDepartment().getId());
        voucher.setName(applicationProperties.getDisposalVoucherName());
        voucher.setDescription(applicationProperties.getDisposalVoucherDescription());
        voucher.setFund(fund);

        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setRequestInfo(new RequestInfo());
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }
}
