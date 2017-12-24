package org.egov.egf.bill.web.repository;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.AccountDetailKey;
import org.egov.egf.bill.web.contract.RequestInfoWrapper;
import org.egov.egf.bill.web.requests.AccountDetailKeyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountDetailKeyRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-master/accountdetailkeys/_search?";

    public AccountDetailKeyRepository(@Value("${egf.master.host.url}") String hostUrl,
            RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public AccountDetailKey findById(AccountDetailKey accountDetailKey, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (accountDetailKey.getId() != null) {
            content.append("id=" + accountDetailKey.getId());
        }

        if (accountDetailKey.getTenantId() != null) {
            content.append("&tenantId=" + accountDetailKey.getTenantId());
        }
        url = url + content.toString();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        AccountDetailKeyResponse result;

        result = restTemplate.postForObject(url, requestInfoWrapper, AccountDetailKeyResponse.class);

        if (result.getAccountDetailKeys() != null && result.getAccountDetailKeys().size() == 1) {
            return result.getAccountDetailKeys().get(0);
        } else {
            return null;
        }

    }

    public List<AccountDetailKey> findByIds(AccountDetailKey accountDetailKey, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (accountDetailKey.getIds() != null) {
            content.append("ids=" + accountDetailKey.getIds());
        }

        if (accountDetailKey.getTenantId() != null) {
            content.append("&tenantId=" + accountDetailKey.getTenantId());
        }
        url = url + content.toString();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        AccountDetailKeyResponse result;

        result = restTemplate.postForObject(url, requestInfoWrapper, AccountDetailKeyResponse.class);

        if (result.getAccountDetailKeys() != null && !result.getAccountDetailKeys().isEmpty()) {
            return result.getAccountDetailKeys();
        } else {
            return null;
        }

    }
}