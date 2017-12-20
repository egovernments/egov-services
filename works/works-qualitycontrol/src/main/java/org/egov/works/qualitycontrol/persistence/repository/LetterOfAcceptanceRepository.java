package org.egov.works.qualitycontrol.persistence.repository;

import org.egov.works.qualitycontrol.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LetterOfAcceptanceRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String searchLoaUrl;

    private String searchLoaByLoaNumberUrl;

    public LetterOfAcceptanceRepository(final RestTemplate restTemplate,@Value("${egov.services.egov_workorder.service.hostname}") final String workOrderHostname,
                                        @Value("${egov.services.egov_workorder.service.searchloa}") final String searchLoaUrl,
                                        @Value("${egov.services.egov_workorder.service.searchloabyloanumber}") final String searchLoaByLoaNumberUrl) {
        this.restTemplate = restTemplate;
        this.searchLoaUrl = workOrderHostname + searchLoaUrl;
        this.searchLoaByLoaNumberUrl = workOrderHostname + searchLoaByLoaNumberUrl;
    }

    public List<LetterOfAcceptance> searchLOA(final String tenantId, final List<String> loaIds, final RequestInfo requestInfo) {
        String status = LOAStatus.APPROVED.toString();
        String letterOfAcceptances = loaIds.stream().map(i -> i.toString()).collect(Collectors.joining(","));
        return restTemplate.postForObject(searchLoaUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId, letterOfAcceptances, status).getLetterOfAcceptances();
    }

    public List<LetterOfAcceptance> searchLOAByLoaNumber(final String tenantId, final List<String> LoaNumbers, final RequestInfo requestInfo) {
        String status = LOAStatus.APPROVED.toString();
        String letterOfAcceptances = LoaNumbers.stream().map(i -> i.toString()).collect(Collectors.joining(","));
        return restTemplate.postForObject(searchLoaByLoaNumberUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId, letterOfAcceptances, status).getLetterOfAcceptances();
    }
}

