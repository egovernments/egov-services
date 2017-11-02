package org.pgr.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.ComplaintMessageQueueRepository;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.TenantRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.repository.contract.Tenant;
import org.pgr.batch.service.model.SevaRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class EscalationService {

    private static final String VALUES_ASSIGNEE_ID = "positionId";
    private static final String VALUES_KEYWORD = "keyword";

    private ComplaintRestRepository complaintRestRepository;

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    private TenantRepository tenantRepository;

    private WorkflowService workflowService;

    private UserService userService;

    private PositionService positionService;

    private EscalationDateService escalationDateService;

    public EscalationService(ComplaintRestRepository complaintRestRepository,
                             WorkflowService workflowService,
                             UserService userService, PositionService positionService,
                             EscalationDateService escalationDateService,
                             ComplaintMessageQueueRepository complaintMessageQueueRepository,
                             TenantRepository tenantRepository) {
        this.complaintRestRepository = complaintRestRepository;
        this.workflowService = workflowService;
        this.userService = userService;
        this.positionService = positionService;
        this.escalationDateService = escalationDateService;
        this.complaintMessageQueueRepository = complaintMessageQueueRepository;
        this.tenantRepository = tenantRepository;
    }

    //This method will fetch all tenant information from tenant service and escalate
    // all eligible complaints per tenant
    public void escalateComplaintForAllTenants() {
        List<Tenant> tenantList = tenantRepository.getAllTenants().getTenant();
        if (!CollectionUtils.isEmpty(tenantList))
            tenantList.forEach(tenant -> escalateComplaintForTenant(tenant.getCode()));
    }

    //Method to fetch all eligible complaints in one tenant
    private void escalateComplaintForTenant(String tenantId) {
        Long userId = getRequestInfo(tenantId).getUserInfo().getId();
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation(tenantId, userId)
                .getServiceRequests();
        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest, tenantId));
    }

    //method to escalate complaint
    private void escalate(ServiceRequest serviceRequest, String tenantId) {
        try {
            validateAndLog(serviceRequest);
            serviceRequest.setPreviousAssignee(serviceRequest.getPositionId());
            workflowService.enrichWorkflowForEscalation(serviceRequest, getRequestInfo(tenantId));

            if (serviceRequest.isNewAssigneeSameAsPreviousAssignee()) {
                log.info("Skipping escalation for CRN {} since new assignee {} is the same",
                        serviceRequest.getCrn(), serviceRequest.getPositionId());
                return;
            }

            positionService.enrichRequestWithPosition(serviceRequest);
            SevaRequest enrichedSevaRequest = new SevaRequest(getRequestInfo(tenantId), serviceRequest);
            escalationDateService.enrichRequestWithEscalationDate(enrichedSevaRequest);
            enrichedSevaRequest.markEscalated();
            complaintMessageQueueRepository.save(enrichedSevaRequest);
        } catch (Exception exception) {
            final String message = String
                    .format("For CRN %s and TenantId %s", serviceRequest.getCrn(), serviceRequest.getTenantId());
            log.error(message, exception);
        }
    }

    private void validateAndLog(ServiceRequest serviceRequest) {
        if (!serviceRequest.isAttributeEntryPresent(VALUES_ASSIGNEE_ID))
            log.warn("FOR CRN" + serviceRequest.getCrn() + "and Tenant" + serviceRequest.getTenantId()
                    + VALUES_ASSIGNEE_ID + "Is Not Present");
        if (!serviceRequest.isAttributeEntryPresent(VALUES_KEYWORD))
            log.warn("FOR CRN" + serviceRequest.getCrn() + "and Tenant" + serviceRequest.getTenantId()
                    + VALUES_KEYWORD + "Is Not Present");
        if (null == serviceRequest.getEscalationDate())
            log.warn("FOR CRN" + serviceRequest.getCrn() + "and Tenant" + serviceRequest.getTenantId()
                    + "Escalation Date Is Not Present");
    }

    private RequestInfo getRequestInfo(String tenantId) {
        return RequestInfo.builder()
                .action("PUT")
                .userInfo(userService.getUserByUserName("system", tenantId))
                .build();
    }
}
