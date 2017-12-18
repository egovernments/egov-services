package org.egov.works.qualitycontrol.persistence.repository;

import org.egov.works.qualitycontrol.web.contract.DetailedEstimate;
import org.egov.works.qualitycontrol.web.contract.DetailedEstimateResponse;
import org.egov.works.qualitycontrol.web.contract.DetailedEstimateStatus;
import org.egov.works.qualitycontrol.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DetailedEstimateRepository {

    private RestTemplate restTemplate;

    private String searchEstimateUrl;

    public DetailedEstimateRepository(final RestTemplate restTemplate,@Value("${egov.services.egov_works_estimate.hostname}") final String estimateServiceHost,
                                      @Value("${egov.services.egov_works_estimate.searchestimateurl}") final String searchEstimateUrl) {
        this.restTemplate = restTemplate;
        this.searchEstimateUrl = estimateServiceHost + searchEstimateUrl;
    }

    public List<DetailedEstimate> searchDetailedEsimates(final String tenantId,final List<String> workIdentificationNumbers, final RequestInfo requestInfo) {
        String winNumbers = String.join(",", workIdentificationNumbers);
        String status = DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString();
        return restTemplate.postForObject(searchEstimateUrl, requestInfo, DetailedEstimateResponse.class, tenantId, winNumbers, status).getDetailedEstimates();
    }
}
