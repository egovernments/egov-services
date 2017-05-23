package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintRestRepository;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.ServiceRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
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

    public SevaRequest enrichWorkflow(SevaRequest sevaRequest) {
        WorkflowRequest request = sevaRequest.getWorkFlowRequest();
        WorkflowResponse workflowResponse = null;
        if (request.isCreate() && sevaRequest.isCreate()) {
            workflowResponse = workflowRepository.create(request);
        } else if (request.isClosed()) {
            workflowResponse = workflowRepository.close(request);
        } else {
            ServiceRequest responseFromDB = complaintRestRepository.getComplaintByCrn(request.getTenantId(), request.getCrn());
            String locationId = responseFromDB.getDynamicSingleValue("locationId");
            String departmentId = responseFromDB.getDynamicSingleValue("departmentId");
            String assigneeId = responseFromDB.getDynamicSingleValue("assignmentId");
            String status = responseFromDB.getDynamicSingleValue("status");
            boolean isUpdate = false;
            if (!responseFromDB.getComplaintTypeCode().equals(request.getValueForKey("complaintTypeCode"))
                || !locationId.equals(request.getValueForKey("boundaryId"))
                || !assigneeId.equals(request.getAssignee().toString())) {
                isUpdate = true;
            } else if (!departmentId.equals(request.getValueForKey("departmentId"))
                || (!status.equals(request.getStatus())
                || !responseFromDB.getDescription().equals(request.getValueForKey("approvalComments")))
                && !isUpdate) {
                isUpdate = true;
            }
            if (isUpdate) {
                workflowResponse = workflowRepository.update(request);
            }

		}
		if (workflowResponse != null)
			sevaRequest.update(workflowResponse);

		return sevaRequest;
	}

}
