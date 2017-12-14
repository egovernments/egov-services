package org.egov.works.masters.web.repository;

import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.works.masters.web.contract.DetailedEstimate;
import org.egov.works.masters.web.contract.DetailedEstimateResponse;
import org.egov.works.masters.web.contract.RequestInfo;
import org.egov.works.masters.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ramki on 12/12/17.
 */

@Repository
public class DetailedEstimateRepository {
    @Autowired
    private final LogAwareRestTemplate restTemplate;

    private final String detailedEstimateSearchUrl;

    @Autowired
    public DetailedEstimateRepository(final LogAwareRestTemplate restTemplate,
                                      @Value("${egov.services.egov_works_estimate.hostname}") final String deServiceHostname,
                                      @Value("${egov.services.egov_works_estimate.searchpath}") final String deBySearchCriteriaUrl) {

        this.restTemplate = restTemplate;
        this.detailedEstimateSearchUrl = deServiceHostname + deBySearchCriteriaUrl;
    }

    public List<DetailedEstimate> searchDetailedEstimatesBySOR(final String sorId, final Long sorFromDate, final Long sorToDate, final String tenantId,
                                                               final RequestInfo requestInfo) {
        StringBuilder url = new StringBuilder();
        url.append(detailedEstimateSearchUrl).append(tenantId).append("&").append("scheduleOfRate=").append(sorId).append("&").append("fromDate=").append(sorFromDate).append("&").append("toDate=").append(sorToDate);

        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        DetailedEstimateResponse deResponse = restTemplate.postForObject(url.toString(), requestInfoWrapper, DetailedEstimateResponse.class);
        return deResponse.getDetailedEstimates();
    }
}
