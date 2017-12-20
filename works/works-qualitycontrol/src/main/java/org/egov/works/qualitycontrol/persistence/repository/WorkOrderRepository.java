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

    private String searchWorkOrderUrlByLOA;

    public WorkOrderRepository(final RestTemplate restTemplate, @Value("${egov.services.egov_workorder.service.hostname}") final String workOrderServiceHost,
                               @Value("${egov.services.egov_workorder.service.searchworkorder}") final String searchWorkOrderUrl,
                               @Value("${egov.services.egov_workorder.service.searchworkorderbyloa}") final String searchWorkOrderUrlByLOA) {
        this.restTemplate = restTemplate;
        this.searchWorkOrderUrl = workOrderServiceHost + searchWorkOrderUrl;
        this.searchWorkOrderUrlByLOA = workOrderServiceHost + searchWorkOrderUrlByLOA;
    }

    public List<WorkOrder> searchWorkOrder(final String tenantId, List<String> workOrderNumbers, final RequestInfo requestInfo) {
        String workOrders = workOrderNumbers.stream().map(i -> i.toString()).collect(Collectors.joining(","));
        String status = WorkOrderStatus.APPROVED.toString();
        return restTemplate.postForObject(searchWorkOrderUrl, requestInfo, WorkOrderResponse.class, tenantId, workOrders, status).getWorkOrders();
    }

    public List<WorkOrder> searchWorkorderByLOA(final String tenantId, final List<String> loaIds, final RequestInfo requestInfo) {
        String status = WorkOrderStatus.APPROVED.toString();
        String letterOfAcceptances = loaIds.stream().map(i -> i.toString()).collect(Collectors.joining(","));
        return restTemplate.postForObject(searchWorkOrderUrlByLOA, requestInfo, WorkOrderResponse.class, tenantId, letterOfAcceptances, status).getWorkOrders();
    }
}
