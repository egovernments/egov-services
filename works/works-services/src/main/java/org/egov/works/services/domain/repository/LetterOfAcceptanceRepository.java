package org.egov.works.services.domain.repository;

import org.egov.works.services.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.services.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LetterOfAcceptanceRepository {

    private RestTemplate restTemplate;

    private String loaSearchUrl;

    public LetterOfAcceptanceRepository(final RestTemplate restTemplate,
            @Value("${egov.services.workorder.hostname}") final String workOrderHostname,
            @Value("${egov.services.egov_works_loa.searchpath}") final String loaSearchUrl) {
        this.restTemplate = restTemplate;
        this.loaSearchUrl = workOrderHostname + loaSearchUrl;
    }

    public LetterOfAcceptanceResponse searchLOAByLOANumber(final LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract, String tenantId,
            RequestInfo requestInfo) {
        final String loaNumber = letterOfAcceptanceSearchContract.getLoaNumbers().get(0);
        final String status = letterOfAcceptanceSearchContract.getStatuses().get(0);
        return restTemplate.postForObject(loaSearchUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId,
                loaNumber, status);
    }

}
