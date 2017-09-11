package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.requests.BudgetGroupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BudgetGroupContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-masters/budgetgroups/_search?";

	public BudgetGroupContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public BudgetGroupContract findById(BudgetGroupContract budgetGroupContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (budgetGroupContract.getId() != null) {
			content.append("id=" + budgetGroupContract.getId());
		}

		if (budgetGroupContract.getTenantId() != null) {
			content.append("&tenantId=" + budgetGroupContract.getTenantId());
		}
		url = url + content.toString();
		BudgetGroupResponse result = restTemplate.postForObject(url, null, BudgetGroupResponse.class);

		if (result.getBudgetGroups() != null && result.getBudgetGroups().size() == 1) {
			return result.getBudgetGroups().get(0);
		} else {
			return null;
		}

	}
}