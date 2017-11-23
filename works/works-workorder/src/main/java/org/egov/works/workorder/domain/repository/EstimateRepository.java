package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.DetailedEstimateResponse;
import org.egov.works.workorder.web.contract.DetailedEstimateSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class EstimateRepository {

	private final RestTemplate restTemplate;

	private final String detailedEstimateUrl;

    private String detailedEstimateByDepartmentUrl;

	@Autowired
	public EstimateRepository(final RestTemplate restTemplate,
			@Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
			@Value("${egov.services.egov_works_estimate.searchpath}") final String detailedEstimateUrl,
            @Value("${egov.services.egov_works_estimate.searchbydepartment}") final String detailedEstimateByDepartmentUrl) {

		this.restTemplate = restTemplate;
		this.detailedEstimateUrl = worksEstimateHostname + detailedEstimateUrl;
        this.detailedEstimateByDepartmentUrl = worksEstimateHostname + detailedEstimateByDepartmentUrl;
	}

	public DetailedEstimateResponse getDetailedEstimateById(
			final DetailedEstimateSearchContract detailedEstimateSearchContract, final String tenantId,
			final RequestInfo requestInfo) {

		StringBuilder url = new StringBuilder();
		url.append(detailedEstimateUrl).append(tenantId).append("&").append("detailedEstimateNumbers=").append(detailedEstimateSearchContract.getDetailedEstimateNumbers().get(0));

		return restTemplate.postForObject(url.toString(), requestInfo, DetailedEstimateResponse.class);

	}

    public List<DetailedEstimate> searchDetailedEstimatesByDepartment(final List<String> departmentCodes, final String tenantId,final RequestInfo requestInfo) {

        return restTemplate.postForObject(detailedEstimateByDepartmentUrl, requestInfo, DetailedEstimateResponse.class,tenantId,departmentCodes).getDetailedEstimates();
    }

}
