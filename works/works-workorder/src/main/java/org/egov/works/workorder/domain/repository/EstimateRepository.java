package org.egov.works.workorder.domain.repository;

import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class EstimateRepository {

	private final LogAwareRestTemplate restTemplate;

	private final String detailedEstimateUrl;

    private String detailedEstimateByDepartmentUrl;

	private String detailedEstimateByWINUrl;

	@Autowired
	public EstimateRepository(final LogAwareRestTemplate restTemplate,
			@Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
			@Value("${egov.services.egov_works_estimate.searchpath}") final String detailedEstimateUrl,
            @Value("${egov.services.egov_works_estimate.searchbydepartment}") final String detailedEstimateByDepartmentUrl,
	        @Value("${egov.services.egov_works_estimate.searchbywin}") final String detailedEstimateByWINUrl) {

		this.restTemplate = restTemplate;
		this.detailedEstimateUrl = worksEstimateHostname + detailedEstimateUrl;
        this.detailedEstimateByDepartmentUrl = worksEstimateHostname + detailedEstimateByDepartmentUrl;
        this.detailedEstimateByWINUrl = worksEstimateHostname + detailedEstimateByWINUrl;
	}

	public DetailedEstimateResponse getDetailedEstimateByEstimateNumber(
			final DetailedEstimateSearchContract detailedEstimateSearchContract, final String tenantId,
			final RequestInfo requestInfo) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		StringBuilder url = new StringBuilder();
		url.append(detailedEstimateUrl).append(tenantId).append("&").append("detailedEstimateNumbers=").append(detailedEstimateSearchContract.getDetailedEstimateNumbers().get(0));

		return restTemplate.postForObject(url.toString(), requestInfoWrapper, DetailedEstimateResponse.class);

	}

    public List<DetailedEstimate> searchDetailedEstimatesByDepartment(final List<String> departmentCodes, final String tenantId,final RequestInfo requestInfo) {

        String status = CommonConstants.STATUS_TECH_SANCTIONED;
        String departments = String.join(",", departmentCodes);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(detailedEstimateByDepartmentUrl, requestInfoWrapper, DetailedEstimateResponse.class, tenantId, departments, status).getDetailedEstimates();
    }

	public List<DetailedEstimate> searchDetailedEstimatesByProjectCode(final List<String> winCodes, final String tenantId,final RequestInfo requestInfo) {

		String status = CommonConstants.STATUS_TECH_SANCTIONED;
		return restTemplate.postForObject(detailedEstimateByWINUrl, requestInfo, DetailedEstimateResponse.class, tenantId, winCodes, status).getDetailedEstimates();
	}

}
