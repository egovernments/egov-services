package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.requests.ChartOfAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChartOfAccountContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = " /egf-master/chartofaccounts/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public ChartOfAccountContractRepository(@Value("${egf.masterhost.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public ChartOfAccountContract findById(ChartOfAccountContract chartOfAccountContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (chartOfAccountContract.getId() != null) {
			content.append("id=" + chartOfAccountContract.getId());
		}

		if (chartOfAccountContract.getTenantId() != null) {
			content.append("&tenantId=" + chartOfAccountContract.getTenantId());
		}
		url = url + content.toString();
		ChartOfAccountResponse result = restTemplate.postForObject(url, null, ChartOfAccountResponse.class);

		if (result.getChartOfAccounts() != null && result.getChartOfAccounts().size() == 1) {
			return result.getChartOfAccounts().get(0);
		} else {
			return null;
		}

	}
}