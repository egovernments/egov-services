package org.egov.collection.repository;

import org.egov.collection.web.contract.ChartOfAccount;
import org.egov.collection.web.contract.ChartOfAccountsResponse;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChartOfAccountsRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String url;

    public ChartOfAccountsRepository(final RestTemplate restTemplate,@Value("${egov.egfmasters.hostname}") final String egfServiceHost,
                                     @Value("${coa.search.uri}") final String url) {
        this.restTemplate = restTemplate;
        this.url = egfServiceHost + url;

    }

    public List<ChartOfAccount> getChartOfAccounts(final List<String> chartOfAccountCodes,final String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(url, requestInfoWrapper,
                ChartOfAccountsResponse.class,tenantId,chartOfAccountCodes.get(0).toString()).getChartOfAccounts();
    }
}
