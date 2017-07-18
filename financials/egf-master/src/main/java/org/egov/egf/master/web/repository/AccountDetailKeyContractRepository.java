package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountDetailKeyContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/accountdetailkeys/_search?";
	@Autowired
	private ObjectMapper objectMapper;

	public AccountDetailKeyContractRepository(@Value("${egf.master.host.url}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public AccountDetailKeyContract findById(AccountDetailKeyContract accountDetailKeyContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (accountDetailKeyContract.getId() != null) {
			content.append("id=" + accountDetailKeyContract.getId());
		}

		if (accountDetailKeyContract.getTenantId() != null) {
			content.append("tenantId=" + accountDetailKeyContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<AccountDetailKeyContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountDetailKeyContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1) {
			return result.getData().get(0);
		} else {
			return null;
		}

	}
}