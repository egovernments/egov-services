package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.ComplaintMessageQueueRepository;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.service.model.SevaRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationService {

    private ComplaintRestRepository complaintRestRepository;

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    private WorkflowService workflowService;

    private UserService userService;

    private PositionService positionService;

    private EscalationDateService escalationDateService;

    public EscalationService( ComplaintRestRepository complaintRestRepository,
                              WorkflowService workflowService,
                              UserService userService,PositionService positionService,
                              EscalationDateService escalationDateService,
                              ComplaintMessageQueueRepository complaintMessageQueueRepository){
        this.complaintRestRepository = complaintRestRepository;
        this.workflowService = workflowService;
        this.userService = userService;
        this.positionService = positionService;
        this.escalationDateService = escalationDateService;
        this.complaintMessageQueueRepository = complaintMessageQueueRepository;
    }

    public void escalateComplaint(){
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation("default").getServiceRequests();
        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest));
    }

    private void escalate(ServiceRequest serviceRequest){
        serviceRequest.setPreviousAssignee(serviceRequest.getAssigneeId());
        serviceRequest =  workflowService.enrichWorkflowForEscalation(serviceRequest,getRequestInfo());
        positionService.enrichRequestWithPosition(serviceRequest);
        SevaRequest enrichedSevaRequest = new SevaRequest(getRequestInfo(), serviceRequest);
        escalationDateService.enrichRequestWithEscalationDate(enrichedSevaRequest);
        complaintMessageQueueRepository.save(enrichedSevaRequest);

        //Escalation should not be done if next assignee is same.
//        if()
    }

    private RequestInfo getRequestInfo(){

        return RequestInfo.builder()
                .action("PUT")
                .userInfo(userService.getUserByUserName("system","default"))
                .build();
    }
}
