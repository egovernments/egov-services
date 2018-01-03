package org.egov.works.services.domain.repository;

import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.WorkOrderResponse;
import org.egov.works.services.web.contract.WorkOrderSearchContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkOrderRepository {

    private RestTemplate restTemplate;

    private String workOrderSearchUrl;

    public WorkOrderRepository(final RestTemplate restTemplate,
            @Value("${egov.services.workorder.hostname}") final String workOrderHostname,
            @Value("${egov.services.egov_works_workorder.searchpath}") final String workOrderSearchUrl) {
        this.restTemplate = restTemplate;
        this.workOrderSearchUrl = workOrderHostname + workOrderSearchUrl;
    }

    public WorkOrderResponse searchWorkOrder(final WorkOrderSearchContract workOrderSearchContract, String tenantId,
            RequestInfo requestInfo) {
        String status = workOrderSearchContract.getStatuses().get(0);
        String workOrderNumber = workOrderSearchContract.getWorkIdentificationNumbers() != null ? String.join(",", workOrderSearchContract.getWorkIdentificationNumbers()) : "";
        return restTemplate.postForObject(workOrderSearchUrl, requestInfo, WorkOrderResponse.class, tenantId,
                workOrderNumber, status);
    }
}
