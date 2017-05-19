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

    public EscalationService( ComplaintRestRepository complaintRestRepository,
                              WorkflowService workflowService,
                              UserService userService){
        this.complaintRestRepository = complaintRestRepository;
        this.workflowService = workflowService;
        this.userService = userService;
    }

    public void escalateComplaint(){
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation("default").getServiceRequests();
        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest));
    }

    private void escalate(ServiceRequest serviceRequest){

        serviceRequest.getValues().put(PREVIOUS_ASSIGNEE, serviceRequest.getAssigneeId());
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userService.getUserByUserName("system","default"));
        serviceRequest =  workflowService.enrichWorkflowForEscalation(serviceRequest,requestInfo);

        //Escalation should not be done if next assignee is same.
//        if()
    }
}
