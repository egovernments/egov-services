package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.requests.FundsourceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FundsourceContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-masters/fundsources/_search?";

	public FundsourceContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public FundsourceContract findById(FundsourceContract fundsourceContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (fundsourceContract.getId() != null) {
			content.append("id=" + fundsourceContract.getId());
		}

		if (fundsourceContract.getTenantId() != null) {
			content.append("&tenantId=" + fundsourceContract.getTenantId());
		}
		url = url + content.toString();
		FundsourceResponse result = restTemplate.postForObject(url, null, FundsourceResponse.class);

		if (result.getFundsources() != null && result.getFundsources().size() == 1) {
			return result.getFundsources().get(0);
		} else {
			return null;
		}

	}
}