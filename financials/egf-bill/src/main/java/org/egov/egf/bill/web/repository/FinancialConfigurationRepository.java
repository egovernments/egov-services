package org.egov.egf.bill.web.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.FinancialConfiguration;
import org.egov.egf.bill.web.contract.RequestInfoWrapper;
import org.egov.egf.bill.web.requests.FinancialConfigurationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class FinancialConfigurationRepository {

    public static final String SEARCH_URL = "/egf-master/financialconfigurations/_search?";
    private RestTemplate restTemplate;
    private String hostUrl;
    private String fetchDataFrom;

    public FinancialConfigurationRepository(@Value("${egf.master.host.url}") String hostUrl,
            @Value("${fetch_data_from}") String fetchDataFrom, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
        this.fetchDataFrom = fetchDataFrom;
    }

    public FinancialConfiguration findById(FinancialConfiguration financialConfiguration, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (financialConfiguration.getId() != null) {
            content.append("id=" + financialConfiguration.getId());
        }

        if (financialConfiguration.getTenantId() != null) {
            content.append("&tenantId=" + financialConfiguration.getTenantId());
        }
        url = url + content.toString();
        FinancialConfigurationResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        result = restTemplate.postForObject(url, requestInfoWrapper, FinancialConfigurationResponse.class);

        if (result.getFinancialConfigurations() != null && result.getFinancialConfigurations().size() == 1) {
            return result.getFinancialConfigurations().get(0);
        } else {
            return null;
        }

    }

    public FinancialConfiguration findByModuleAndName(FinancialConfiguration financialConfiguration, RequestInfo requestInfo) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (financialConfiguration.getName() != null) {
            content.append("name=" + financialConfiguration.getName());
        }

        if (financialConfiguration.getModule() != null) {
            content.append("&module=" + financialConfiguration.getModule());
        }

        if (financialConfiguration.getTenantId() != null) {
            content.append("&tenantId=" + financialConfiguration.getTenantId());
        }
        url = url + content.toString();
        FinancialConfigurationResponse result;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        result = restTemplate.postForObject(url, requestInfoWrapper, FinancialConfigurationResponse.class);

        if (result.getFinancialConfigurations() != null && result.getFinancialConfigurations().size() == 1) {
            return result.getFinancialConfigurations().get(0);
        } else {
            return null;
        }

    }

    public String fetchDataFrom() {
        return fetchDataFrom;
    }

}