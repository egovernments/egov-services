package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BudgetGroupContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/budgetgroups/_search?";
	@Autowired
	private ObjectMapper objectMapper;

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
		CommonResponse<BudgetGroupContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<BudgetGroupContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1) {
			return result.getData().get(0);
		} else {
			return null;
		}

	}
}