package org.egov.egf.master.web.contract.repository;

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
	public static final String SEARCH_URL = "/egf-master/financialyears/_search?id={id}";

	@Autowired
	private ObjectMapper objectMapper;

	public FinancialYearContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public CommonResponse<FinancialYearContract> getFinancialYearById(String id) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);

		CommonResponse<FinancialYearContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class, id),
				new TypeReference<CommonResponse<FinancialYearContract>>() {
				});

		return result;

	}
}
