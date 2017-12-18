package org.egov.works.qualitycontrol.persistence.repository;

import org.egov.works.qualitycontrol.web.contract.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderRepository {

    private RestTemplate restTemplate;

    private String searchWorkOrderUrl;

    public WorkOrderRepository(final RestTemplate restTemplate, @Value("${egov.services.egov_workorder.service.hostname}") final String workOrderServiceHost,
                               @Value("${egov.services.egov_workorder.service.searchworkorder}") final String searchWorkOrderUrl) {
        this.restTemplate = restTemplate;
        this.searchWorkOrderUrl = workOrderServiceHost + searchWorkOrderUrl;
    }

    public List<WorkOrder> searchWorkOrder(final String tenantId, List<String> workOrderNumbers, final RequestInfo requestInfo) {
        String workOrders = workOrderNumbers.stream().map(i -> i.toString()).collect(Collectors.joining(","));
        String status = WorkOrderStatus.APPROVED.toString();
        return restTemplate.postForObject(searchWorkOrderUrl, requestInfo, WorkOrderResponse.class, tenantId, workOrders, status).getWorkOrders();
    }
}
