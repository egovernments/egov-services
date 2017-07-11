package org.egov.egf.master.web.contract.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.common.web.contract.RequestInfo;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class FinancialYearContractRepository {

	public static int DEFAULT_PAGE_SIZE = 500;
	public static int DEFAULT_PAGE_OFFSET = 0;

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "/egf-master/financialyears/_search?id={id}&pageSize={pageSize}&offset={offSet}";

	@Autowired
	private ObjectMapper objectMapper;

	public FinancialYearContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public CommonResponse<FinancialYearContract> getFinancialYearById(String id, String pageSize, String offSet,
			RequestInfo requestInfo) {

		if (pageSize == null || pageSize.isEmpty())
			pageSize = String.valueOf(DEFAULT_PAGE_SIZE);
		if (offSet == null || offSet.isEmpty())
			offSet = String.valueOf(DEFAULT_PAGE_OFFSET);

		String url = String.format("%s%s", hostUrl, SEARCH_URL);

		CommonResponse<FinancialYearContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, requestInfo, CommonResponse.class, id, pageSize, offSet),
				new TypeReference<CommonResponse<FinancialYearContract>>() {
				});

		return result;

	}
}
