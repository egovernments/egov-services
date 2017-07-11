package org.pgr.batch.service;
import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.WorkflowRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.repository.contract.WorkflowRequest;
import org.pgr.batch.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

	private WorkflowRepository workflowRepository;

	private ComplaintRestRepository complaintRestRepository;

	@Autowired
	public WorkflowService(WorkflowRepository workflowRepository, ComplaintRestRepository complaintRestRepository) {
		this.workflowRepository = workflowRepository;
		this.complaintRestRepository = complaintRestRepository;
	}

	public ServiceRequest enrichWorkflowForEscalation(ServiceRequest serviceRequest, RequestInfo requestInfo) {
		serviceRequest.setPreviousAssignee(serviceRequest.getPositionId());
        WorkflowRequest escalationRequest = serviceRequest.getWorkFlowRequestForEscalation(requestInfo);
        WorkflowResponse workflowResponse = workflowRepository.update(escalationRequest);

        if (workflowResponse != null)
			serviceRequest.update(workflowResponse);

        return serviceRequest;
    }
}
