package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.FinancialConfigurationContract;
import org.egov.egf.master.web.requests.FinancialConfigurationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FinancialConfigurationContractRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-master/financialconfigurations/_search?";

    @Value("${fetch_data_from}")
    private String fetchDataFrom;

    public FinancialConfigurationContractRepository(@Value("${egf.master.host.url}") String hostUrl,
            RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public FinancialConfigurationContract findById(FinancialConfigurationContract financialConfigurationContract) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (financialConfigurationContract.getId() != null) {
            content.append("id=" + financialConfigurationContract.getId());
        }

        if (financialConfigurationContract.getTenantId() != null) {
            content.append("&tenantId=" + financialConfigurationContract.getTenantId());
        }
        url = url + content.toString();
        FinancialConfigurationResponse result = restTemplate.postForObject(url, null,
                FinancialConfigurationResponse.class);

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