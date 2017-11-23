package org.egov.works.measurementbook.domain.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkOrderRepository {

    private RestTemplate restTemplate;

    private String contractorSearchUrl;


    public WorkOrderRepository(final RestTemplate restTemplate,@Value("${egov.services.workorder.hostname}") final String workOrderHostname,
                               @Value("${egov.services.workorder.contractorsearchpath}") final String contractorSearchUrl) {
        this.restTemplate = restTemplate;
        this.contractorSearchUrl = workOrderHostname + contractorSearchUrl;
    }

}
