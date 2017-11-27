package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.web.contract.LOAStatus;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class WorkOrderRepository {

    private RestTemplate restTemplate;

    private String contractorSearchUrl;


    public WorkOrderRepository(final RestTemplate restTemplate,@Value("${egov.services.workorder.hostname}") final String workOrderHostname,
                               @Value("${egov.services.workorder.contractorsearchpath}") final String contractorSearchUrl) {
        this.restTemplate = restTemplate;
        this.contractorSearchUrl = workOrderHostname + contractorSearchUrl;
    }

    public List<LetterOfAcceptance> searchLetterOfAcceptance(List<String> codes,List<String> names, String tenantId,
                                                             RequestInfo requestInfo) {
        String status = LOAStatus.APPROVED.toString();
        String contractorCodes = codes != null ? String.join(",", codes) : "";
        String contractorNames = names != null ? String.join(",", names) : "";
        return restTemplate.postForObject(contractorSearchUrl,requestInfo, LetterOfAcceptanceResponse.class,tenantId, contractorCodes,contractorNames,status).getLetterOfAcceptances();

    }

}
