package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.web.contract.DetailedEstimate;
import org.egov.works.measurementbook.web.contract.DetailedEstimateResponse;
import org.egov.works.measurementbook.web.contract.DetailedEstimateStatus;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class EstimateRepository {

	private final RestTemplate restTemplate;

    private String detailedEstimateByDepartmentUrl;

	@Autowired
	public EstimateRepository(final RestTemplate restTemplate,
                              @Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
                              @Value("${egov.services.egov_works_estimate.searchbydepartment}") final String detailedEstimateByDepartmentUrl) {

		this.restTemplate = restTemplate;
        this.detailedEstimateByDepartmentUrl = worksEstimateHostname + detailedEstimateByDepartmentUrl;
	}

    public List<DetailedEstimate> searchDetailedEstimatesByDepartment(final List<String> departmentCodes, final String tenantId,final RequestInfo requestInfo) {

        String status = DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString();
        return restTemplate.postForObject(detailedEstimateByDepartmentUrl, requestInfo, DetailedEstimateResponse.class,tenantId,departmentCodes, status).getDetailedEstimates();
    }

}
