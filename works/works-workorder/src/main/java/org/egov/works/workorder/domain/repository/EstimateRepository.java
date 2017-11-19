package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.DetailedEstimateResponse;
import org.egov.works.workorder.web.contract.DetailedEstimateSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class EstimateRepository {

	private final RestTemplate restTemplate;

	private final String detailedEstimateUrl;

	@Autowired
	public EstimateRepository(final RestTemplate restTemplate,
			@Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
			@Value("${egov.services.egov_works_estimate.searchpath}") final String detailedEstimateUrl) {

		this.restTemplate = restTemplate;
		this.detailedEstimateUrl = worksEstimateHostname + detailedEstimateUrl;
	}

	public DetailedEstimateResponse getDetailedEstimateById(
			final DetailedEstimateSearchContract detailedEstimateSearchContract, final String tenantId,
			final RequestInfo requestInfo) {

		StringBuilder url = new StringBuilder();
		url.append(detailedEstimateUrl).append(tenantId).append("&").append("ids=").append(detailedEstimateSearchContract.getIds().get(0));
		restTemplate.postForObject(url.toString(), requestInfo, DetailedEstimateResponse.class);

		return restTemplate.postForObject(url.toString(), requestInfo, DetailedEstimateResponse.class);

	}

}
