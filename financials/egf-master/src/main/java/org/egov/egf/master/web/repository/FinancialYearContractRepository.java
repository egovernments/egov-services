package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class FinancialYearContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/financialyears/_search?";

	@Autowired
	private ObjectMapper objectMapper;

	public FinancialYearContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public FinancialYearContract findById(FinancialYearContract financialYearContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (financialYearContract.getId() != null) {
			content.append("id=" + financialYearContract.getId());
		}

		if (financialYearContract.getTenantId() != null) {
			content.append("&tenantId=" + financialYearContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<FinancialYearContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FinancialYearContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
}
