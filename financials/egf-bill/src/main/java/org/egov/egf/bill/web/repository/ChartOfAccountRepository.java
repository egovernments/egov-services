package org.egov.egf.bill.web.repository;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.ChartOfAccount;
import org.egov.egf.bill.web.contract.RequestInfoWrapper;
import org.egov.egf.bill.web.requests.ChartOfAccountResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChartOfAccountRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-master/chartofaccounts/_search?";

    public ChartOfAccountRepository(@Value("${egf.master.host.url}") String hostUrl,
            RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public ChartOfAccount findById(ChartOfAccount chartOfAccount, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (chartOfAccount.getId() != null) {
            content.append("id=" + chartOfAccount.getId());
        }

        if (chartOfAccount.getTenantId() != null) {
            content.append("&tenantId=" + chartOfAccount.getTenantId());
        }
        url = url + content.toString();
        ChartOfAccountResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        result = restTemplate.postForObject(url, requestInfoWrapper, ChartOfAccountResponse.class);

        if (result.getChartOfAccounts() != null && result.getChartOfAccounts().size() == 1) {
            return result.getChartOfAccounts().get(0);
        } else {
            return null;
        }

    }

    public ChartOfAccount findByGlcode(ChartOfAccount chartOfAccountContract, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (chartOfAccountContract.getGlcode() != null) {
            content.append("glcode=" + chartOfAccountContract.getGlcode());
        }

        if (chartOfAccountContract.getTenantId() != null) {
            content.append("&tenantId=" + chartOfAccountContract.getTenantId());
        }
        url = url + content.toString();
        ChartOfAccountResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        result = restTemplate.postForObject(url, requestInfoWrapper, ChartOfAccountResponse.class);

        if (result.getChartOfAccounts() != null && result.getChartOfAccounts().size() == 1) {
            return result.getChartOfAccounts().get(0);
        } else {
            return null;
        }

    }

    public List<ChartOfAccount> findByGlcodes(ChartOfAccount chartOfAccountContract, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (chartOfAccountContract.getGlcodes() != null) {
            content.append("glcodes=" + chartOfAccountContract.getGlcodes());
        }

        if (chartOfAccountContract.getTenantId() != null) {
            content.append("&tenantId=" + chartOfAccountContract.getTenantId());
        }
        url = url + content.toString();
        ChartOfAccountResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        result = restTemplate.postForObject(url, requestInfoWrapper, ChartOfAccountResponse.class);

        if (result.getChartOfAccounts() != null && !result.getChartOfAccounts().isEmpty()) {
            return result.getChartOfAccounts();
        } else {
            return null;
        }

    }
}