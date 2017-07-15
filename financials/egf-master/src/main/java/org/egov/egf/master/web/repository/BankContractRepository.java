package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.BankSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BankContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/Banks/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public BankContractRepository(@Value("${egf.masterhost.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public CommonResponse<BankContract> getBankById(BankSearchContract bankSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (bankSearchContract.getId() != null) {
			content.append("id=" + bankSearchContract.getId());
		}

		if (bankSearchContract.getTenantId() != null) {
			content.append("tenantId=" + bankSearchContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<BankContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<BankContract>>() {
				});

		return result;

	}
}