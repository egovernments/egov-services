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
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.contract.VoucherResponse;
import org.egov.asset.model.ChartOfAccountContract;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.DepreciationInputs;
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
       // headers.setOrigin("http://kurnool-pilot-services.egovernments.org");
        final String createVoucherUrl = applicationProperties.getHostNameForMonolithic(tenantId) + applicationProperties.getEgfServiceVoucherCreatePath()
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

    public VoucherAccountCodeDetails getGlCodes(final RequestInfo requestInfo, final String tenantId,
            final Long accountId, final BigDecimal amount, final Boolean iscredit, final Boolean isDebit) {

        final VoucherAccountCodeDetails debitAccountCodeDetail = new VoucherAccountCodeDetails();
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

        log.debug("Account Code Detail :: " + debitAccountCodeDetail);

        return debitAccountCodeDetail;
    }

    public List<ChartOfAccountContract> getChartOfAccounts(final RequestInfo requestInfo, final String tenantId,
            final Long accountId) {
        final String url = applicationProperties.getEgfMastersHost()
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
        final String url = applicationProperties.getEgfMastersHost()
                + applicationProperties.getEgfServiceChartOfAccountsDetailsSearchPath() + "?tenantId=" + tenantId
                + "&chartOfAccount=" + accountId;
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

    private Voucher generateVoucher(final Long departmentId) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));

        voucher.setDepartment(departmentId);
        return voucher;
    }

    public VoucherRequest createRevaluationVoucherRequest(final Revaluation revaluation,
            final List<VoucherAccountCodeDetails> accountCodeDetails, final Long assetId, final Long departmentId,
            final HttpHeaders header) {
        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<>();
        final String tenantId = revaluation.getTenantId();

        final Voucher voucher = generateVoucher(departmentId);

        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId));

        final String source = header.getOrigin() + "/asset-web/app/asset/create-asset-revaluation.html?id=" + assetId +
                "&revaluationId=" + revaluation.getId() + "&type=view";

        log.debug("Revaluation Source :: " + source);
        voucher.setSource(source);

        final Function function = new Function();
        function.setId(revaluation.getFunction());

        final Fund fund = new Fund();
        fund.setId(revaluation.getFund());

        for (final VoucherAccountCodeDetails acd : accountCodeDetails)
            acd.setFunction(function);

        voucher.setLedgers(accountCodeDetails);

        voucher.setFund(fund);

        final Map<String, String> voucherParams = getVoucherParameters(tenantId);
        setAdditionalFinancialParams(voucherParams, voucher, tenantId);

        log.debug("Revaluation Voucher :: " + voucher);

        vouchers.add(voucher);

        generateVoucherRequest(voucherRequest, vouchers);

        return voucherRequest;
    }

    private void generateVoucherRequest(final VoucherRequest voucherRequest, final List<Voucher> vouchers) {
        final org.egov.asset.model.RequestInfo reqInfo = new org.egov.asset.model.RequestInfo();
        voucherRequest.setRequestInfo(reqInfo);
        voucherRequest.setVouchers(vouchers);
    }

    public VoucherRequest createDisposalVoucherRequest(final Disposal disposal, final Long assetId,
            final Long departmentId, final List<VoucherAccountCodeDetails> accountCodeDetails,final String functionCode,
            final HttpHeaders header) {
        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<>();
        final String tenantId = disposal.getTenantId();

        final Voucher voucher = generateVoucher(departmentId);

        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DISPOSALVOUCHERDESCRIPTION, tenantId));

        final String source = header.getOrigin() + "/asset-web/app/asset/create-asset-sale.html?id=" + assetId
                + "&type=view";

        log.debug("Disposal source :: " + source);
        voucher.setSource(source);

        setFinancialParameters(voucher, accountCodeDetails, tenantId,functionCode);

        voucher.setLedgers(accountCodeDetails);

        log.debug("Disposal Voucher :: " + voucher);

        vouchers.add(voucher);
        generateVoucherRequest(voucherRequest, vouchers);

        return voucherRequest;
    }

    public VoucherRequest createDepreciationVoucherRequest(
            final List<DepreciationInputs> depreciationInputsList, final Long departmentId,final String functionCode,final Long assetId,
           final Long depreciationId,  final List<VoucherAccountCodeDetails> accountCodeDetails, final String tenantId, final HttpHeaders header) {

        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<>();
        
        final Voucher voucher = generateVoucher(departmentId);
        
        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONVOUCHERDESCRIPTION, tenantId));
        final String source = header.getOrigin() + "/asset-web/app/asset/view-asset-depreciation.html?id=" + assetId +
                "&depreciationId=" + depreciationId + "&type=view";

        log.debug("Revaluation Source :: " + source);
        voucher.setSource(source);
        
        setFinancialParameters(voucher, accountCodeDetails, tenantId,functionCode);
       
        voucher.setLedgers(accountCodeDetails);

        log.debug("Depreciation Voucher :: " + voucher);

        vouchers.add(voucher);

        generateVoucherRequest(voucherRequest, vouchers);

        return voucherRequest;
    }

    private void setAdditionalFinancialParams(final Map<String, String> voucherParams, final Voucher voucher,
            final String tenantId) {

        final String functionaryCode = voucherParams.get(AssetFinancialParams.FUNCTIONARY.toString());
        final String schemeCode = voucherParams.get(AssetFinancialParams.SCHEME.toString());
        final String subSchemeCode = voucherParams.get(AssetFinancialParams.SUBSCHEME.toString());
        final String fundSourceCode = voucherParams.get(AssetFinancialParams.FUNDSOURCE.toString());
        final String fiscalName = voucherParams.get(AssetFinancialParams.FISCAL.toString());

        if (functionaryCode != null) {
            final Functionary functionary = new Functionary();
            functionary.setCode(functionaryCode);
            voucher.setFunctionary(functionary);
        }

        if (schemeCode != null) {
            final Scheme scheme = new Scheme();
            scheme.setCode(schemeCode);
            voucher.setScheme(scheme);
        }

        if (subSchemeCode != null) {
            final SubScheme subScheme = new SubScheme();
            subScheme.setCode(subSchemeCode);
            voucher.setSubScheme(subScheme);
        }

        if (fundSourceCode != null) {
            final FundSource fundSource = new FundSource();
            fundSource.setCode(fundSourceCode);
            voucher.setFundsource(fundSource);
        }

        if (fiscalName != null) {
            final FiscalPeriod fiscalPeriod = new FiscalPeriod();
            fiscalPeriod.setName(fiscalName);
            voucher.setFiscalPeriod(fiscalPeriod);
        }

    }

    public void setFinancialParameters(final Voucher voucher, final List<VoucherAccountCodeDetails> accountCodeDetails,
            final String tenantId,final String functionCode) {

        final Map<String, String> voucherParams = getVoucherParameters(tenantId);

        log.debug("Voucher Parameters :: " + voucherParams);

        final String fundCode = voucherParams.get(AssetFinancialParams.FUND.toString());
      
        if (fundCode != null) {
            final Fund fund = new Fund();
            fund.setCode(fundCode);
            voucher.setFund(fund);
        }

        if (functionCode != null) {
            final Function function = new Function();
            function.setCode(functionCode);

            for (final VoucherAccountCodeDetails acd : accountCodeDetails)
                acd.setFunction(function);

            log.debug("account code details :: " + accountCodeDetails);
            voucher.setLedgers(accountCodeDetails);
        }

        setAdditionalFinancialParams(voucherParams, voucher, tenantId);
    }

    private Map<String, String> getVoucherParameters(final String tenantId) {
        final String voucherParamsConfig = assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.VOUCHERPARAMS, tenantId);

        log.debug("Voucher Parameters From Config :: " + voucherParamsConfig);
        final TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };

        Map<String, String> voucherParams = null;
        try {
            voucherParams = mapper.readValue(voucherParamsConfig, typeRef);
        } catch (final JsonParseException e) {
            e.printStackTrace();
        } catch (final JsonMappingException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return voucherParams;
    }

}
