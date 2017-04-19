package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationService {

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
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation(1L);
        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest));
    }

    private void escalate(ServiceRequest serviceRequest){
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userService.getUserByUserName("system"));
        serviceRequest =  workflowService.enrichWorkflowForEscalation(serviceRequest,requestInfo);
    }
}
