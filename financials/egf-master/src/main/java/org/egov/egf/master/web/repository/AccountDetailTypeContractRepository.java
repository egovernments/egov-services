package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountDetailTypeContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = " /egf-master/accountdetailtypes/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public AccountDetailTypeContractRepository(@Value("${egf.master.host.url}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public AccountDetailTypeContract findById(AccountDetailTypeContract accountDetailTypeContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (accountDetailTypeContract.getId() != null) {
			content.append("id=" + accountDetailTypeContract.getId());
		}

		if (accountDetailTypeContract.getTenantId() != null) {
			content.append("tenantId=" + accountDetailTypeContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<AccountDetailTypeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountDetailTypeContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1) {
			return result.getData().get(0);
		} else {
			return null;
		}

	}
}