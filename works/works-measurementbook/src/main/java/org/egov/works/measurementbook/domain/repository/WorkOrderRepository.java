package org.egov.works.measurementbook.domain.repository;

import java.util.List;

import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.egov.works.measurementbook.web.contract.WorkOrderResponse;
import org.egov.works.measurementbook.web.contract.WorkOrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkOrderRepository {

    private RestTemplate restTemplate;

    private String workOrderSearchUrl;
    
    public WorkOrderRepository(final RestTemplate restTemplate,@Value("${egov.services.workorder.hostname}") final String workOrderHostname,
                               @Value("${egov.services.egov_works_workorder.searchpath}") final String workOrderSearchUrl) {
        this.restTemplate = restTemplate;
        this.workOrderSearchUrl = workOrderHostname + workOrderSearchUrl;
    }

	public WorkOrderResponse searchWorkOrder(List<String> loaIds, String tenantId,
			RequestInfo requestInfo) {
		String status = WorkOrderStatus.APPROVED.toString();
		String letterOfAcceptances = loaIds != null ? String.join(",", loaIds) : "";
		return restTemplate.postForObject(workOrderSearchUrl, requestInfo, WorkOrderResponse.class, tenantId,
				letterOfAcceptances, status);
	}
}
