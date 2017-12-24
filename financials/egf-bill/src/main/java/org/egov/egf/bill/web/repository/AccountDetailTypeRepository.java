package org.egov.egf.bill.web.repository;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.AccountDetailType;
import org.egov.egf.bill.web.contract.RequestInfoWrapper;
import org.egov.egf.bill.web.requests.AccountDetailTypeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountDetailTypeRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-master/accountdetailtypes/_search?";

    public AccountDetailTypeRepository(@Value("${egf.master.host.url}") String hostUrl,
            RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public AccountDetailType findById(AccountDetailType accountDetailType, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (accountDetailType.getId() != null) {
            content.append("id=" + accountDetailType.getId());
        }

        if (accountDetailType.getTenantId() != null) {
            content.append("&tenantId=" + accountDetailType.getTenantId());
        }
        url = url + content.toString();
        AccountDetailTypeResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        result = restTemplate.postForObject(url, requestInfoWrapper, AccountDetailTypeResponse.class);

        if (result.getAccountDetailTypes() != null && result.getAccountDetailTypes().size() == 1) {
            return result.getAccountDetailTypes().get(0);
        } else {
            return null;
        }

    }

    public List<AccountDetailType> findByIds(AccountDetailType accountDetailType, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (accountDetailType.getIds() != null) {
            content.append("ids=" + accountDetailType.getIds());
        }

        if (accountDetailType.getTenantId() != null) {
            content.append("&tenantId=" + accountDetailType.getTenantId());
        }
        url = url + content.toString();
        AccountDetailTypeResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        result = restTemplate.postForObject(url, requestInfoWrapper, AccountDetailTypeResponse.class);

        if (result.getAccountDetailTypes() != null && !result.getAccountDetailTypes().isEmpty()) {
            return result.getAccountDetailTypes();
        } else {
            return null;
        }

    }
}