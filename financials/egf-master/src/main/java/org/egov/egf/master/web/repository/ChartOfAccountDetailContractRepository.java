package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.ChartOfAccountDetailContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChartOfAccountDetailContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/chartofaccountdetails/_search?";
	@Autowired
	private ObjectMapper objectMapper;

	public ChartOfAccountDetailContractRepository(@Value("${egf.master.host.url}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public ChartOfAccountDetailContract findById(ChartOfAccountDetailContract chartOfAccountDetailContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (chartOfAccountDetailContract.getId() != null) {
			content.append("id=" + chartOfAccountDetailContract.getId());
		}

		if (chartOfAccountDetailContract.getTenantId() != null) {
			content.append("tenantId=" + chartOfAccountDetailContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<ChartOfAccountDetailContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<ChartOfAccountDetailContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1) {
			return result.getData().get(0);
		} else {
			return null;
		}

	}
}