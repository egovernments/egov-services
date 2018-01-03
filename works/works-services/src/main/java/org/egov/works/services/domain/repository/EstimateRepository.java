package org.egov.works.services.domain.repository;

import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.works.services.web.contract.DetailedEstimateResponse;
import org.egov.works.services.web.contract.DetailedEstimateSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.factory.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class EstimateRepository {

    private final LogAwareRestTemplate restTemplate;

    private final String detailedEstimateUrl;

    @Autowired
    public EstimateRepository(final LogAwareRestTemplate restTemplate,
            @Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
            @Value("${egov.services.egov_works_estimate.searchpath}") final String detailedEstimateUrl) {

        this.restTemplate = restTemplate;
        this.detailedEstimateUrl = worksEstimateHostname + detailedEstimateUrl;
    }

    public DetailedEstimateResponse getDetailedEstimateByEstimateNumber(
            final DetailedEstimateSearchContract detailedEstimateSearchContract, final String tenantId,
            final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        StringBuilder url = new StringBuilder();
        url.append(detailedEstimateUrl).append(tenantId).append("&detailedEstimateNumbers=")
                .append(detailedEstimateSearchContract.getDetailedEstimateNumbers().get(0))
                .append("&statuses").append(detailedEstimateSearchContract.getStatuses().get(0));

        return restTemplate.postForObject(url.toString(), requestInfoWrapper, DetailedEstimateResponse.class);

    }

}
