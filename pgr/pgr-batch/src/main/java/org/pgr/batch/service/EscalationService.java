package org.pgr.batch.service;

import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationService {

    private ComplaintRestRepository complaintRestRepository;

    private WorkflowService workflowService;

    public EscalationService( ComplaintRestRepository complaintRestRepository,
                              WorkflowService workflowService){
        this.complaintRestRepository = complaintRestRepository;
        this.workflowService = workflowService;
    }

    public void escalateComplaint(){
        List<ServiceRequest> serviceRequests = complaintRestRepository.getComplaintsEligibleForEscalation(1L);

        serviceRequests.forEach(serviceRequest -> escalate(serviceRequest));

    }

    private void escalate(ServiceRequest serviceRequest){
        serviceRequest =  workflowService.enrichWorkflowForEscalation(serviceRequest);
    }
}
