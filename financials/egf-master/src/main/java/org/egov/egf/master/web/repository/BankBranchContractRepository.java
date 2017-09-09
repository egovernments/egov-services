package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.BankBranchContract;
import org.egov.egf.master.web.requests.BankBranchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankBranchContractRepository {
	
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/bankbranches/_search?";

	public BankBranchContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public BankBranchContract findById(BankBranchContract bankBranchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (bankBranchContract.getId() != null) {
			content.append("id=" + bankBranchContract.getId());
		}

		if (bankBranchContract.getTenantId() != null) {
			content.append("&tenantId=" + bankBranchContract.getTenantId());
		}
		url = url + content.toString();
		BankBranchResponse result = restTemplate.postForObject(url, null, BankBranchResponse.class);

		if (result.getBankBranches() != null && result.getBankBranches().size() == 1) {
			return result.getBankBranches().get(0);
		} else {
			return null;
		}

	}
}