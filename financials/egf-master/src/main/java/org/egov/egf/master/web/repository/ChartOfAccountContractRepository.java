package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.requests.ChartOfAccountResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChartOfAccountContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-masters/chartofaccounts/_search?";

	public ChartOfAccountContractRepository(@Value("${egf.master.host.url}") String hostUrl,
			RestTemplate restTemplate) {
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

	public ChartOfAccountContract findByGlcode(ChartOfAccountContract chartOfAccountContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (chartOfAccountContract.getGlcode() != null) {
			content.append("glcode=" + chartOfAccountContract.getGlcode());
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