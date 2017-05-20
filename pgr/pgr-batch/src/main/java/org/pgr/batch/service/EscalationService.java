package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationService {

    public static final String PREVIOUS_ASSIGNEE = "previousAssignee";

    private ComplaintRestRepository complaintRestRepository;

    private WorkflowService workflowService;

    private UserService userService;

    private PositionService positionService;

    public EscalationService( ComplaintRestRepository complaintRestRepository,
                              WorkflowService workflowService,
                              UserService userService,PositionService positionService){
        this.complaintRestRepository = complaintRestRepository;
        this.workflowService = workflowService;
        this.userService = userService;
        this.positionService = positionService;
    }

    public void escalateComplaint(){
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation("default").getServiceRequests();
        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest));
    }

    private void escalate(ServiceRequest serviceRequest){

        serviceRequest.setPreviousAssignee(serviceRequest.getAssigneeId());
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userService.getUserByUserName("system","default"));
        serviceRequest =  workflowService.enrichWorkflowForEscalation(serviceRequest,requestInfo);
        positionService.enrichRequestWithPosition(serviceRequest);

        //Escalation should not be done if next assignee is same.
//        if()
    }
}
