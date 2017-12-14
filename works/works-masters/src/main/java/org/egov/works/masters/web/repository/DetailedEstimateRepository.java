package org.egov.works.masters.web.repository;

import org.egov.works.masters.web.contract.DetailedEstimate;
import org.egov.works.masters.web.contract.DetailedEstimateResponse;
import org.egov.works.masters.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by ramki on 12/12/17.
 */

@Repository
public class DetailedEstimateRepository {
    @Autowired
    private final RestTemplate restTemplate;

    private final String detailedEstimateSearchUrl;

    @Autowired
    public DetailedEstimateRepository(final RestTemplate restTemplate,
                                      @Value("${egov.services.egov_works_estimate.hostname}") final String deServiceHostname,
                                      @Value("${egov.services.egov_works_estimate.searchpath}") final String deBySearchCriteriaUrl) {

        this.restTemplate = restTemplate;
        this.detailedEstimateSearchUrl = deServiceHostname + deBySearchCriteriaUrl;
    }

    public List<DetailedEstimate> searchDetailedEstimatesBySOR(final String sorId, final Long sorDate, final String tenantId,
                                                               final RequestInfo requestInfo) {
        DetailedEstimateResponse deResponse = restTemplate.postForObject(detailedEstimateSearchUrl, requestInfo, DetailedEstimateResponse.class, tenantId, sorId, sorDate);
        return deResponse.getDetailedEstimates();
    }
}
