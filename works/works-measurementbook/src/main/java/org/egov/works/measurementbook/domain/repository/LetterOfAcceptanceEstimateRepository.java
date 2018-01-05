package org.egov.works.measurementbook.domain.repository;

import java.util.List;

import org.egov.works.measurementbook.web.contract.LOAStatus;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LetterOfAcceptanceEstimateRepository {
    
    private RestTemplate restTemplate;

    private String contractorSearchUrl;
    
    private String loaeSearchUrl;
    
    @Autowired
    private ObjectMapper objectMapper;
/*

    public LetterOfAcceptanceEstimateRepository(final RestTemplate restTemplate,@Value("${egov.services.workorder.hostname}") final String workOrderHostname,
                               @Value("${egov.services.workorder.contractorsearchpath}") final String contractorSearchUrl,
                               @Value("${egov.services.egov_works_loae.searchpath}") final String loaeSearchUrl) {
        this.restTemplate = restTemplate;
        this.contractorSearchUrl = workOrderHostname + contractorSearchUrl;
        this.loaeSearchUrl = workOrderHostname + loaeSearchUrl;
    }

    public List<LetterOfAcceptance> searchLetterOfAcceptance(List<String> codes,List<String> names, String tenantId,
                                                             RequestInfo requestInfo) {
        String status = LOAStatus.APPROVED.toString();
        String contractorCodes = codes != null ? String.join(",", codes) : "";
        String contractorNames = names != null ? String.join(",", names) : "";
        return restTemplate.postForObject(contractorSearchUrl,requestInfo, LetterOfAcceptanceResponse.class,tenantId, contractorCodes,contractorNames,status).getLetterOfAcceptances();

    }
    
    public LetterOfAcceptanceResponse searchLOAById(List<String> idList, String tenantId,
            RequestInfo requestInfo) {
        String status = LOAStatus.APPROVED.toString();
        String ids = idList != null ? String.join(",", idList) : "";
        return restTemplate.postForObject(loaeSearchUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId,
                ids, status);
    }
*/
}
