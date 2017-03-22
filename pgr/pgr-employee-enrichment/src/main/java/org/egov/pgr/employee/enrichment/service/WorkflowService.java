package org.egov.pgr.employee.enrichment.service;

import java.util.Map;

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
		if (WorkflowRequest.Action.isCreate(request.getAction())
				&& sevaRequest.getRequestInfo().getAction().equals("POST")) {
			workflowResponse = workflowRepository.create(request);
		} else if (WorkflowRequest.Action.isEnd(request.getAction())) {
			workflowResponse = workflowRepository.close(request);
		} else {
			ServiceRequest responseFromDB = complaintRestRepository.getComplaintByCrn(1L, request.getCrn());
			Map<String, String> values = responseFromDB.getValues();
			String locationId = values.get("locationId");
//			String department = values.get("departmentName");
			String assigneeId = values.get("assigneeId");
			String status = values.get("complaintStatus");
			boolean isUpdate = false;
			if (!responseFromDB.getComplaintTypeCode().equals(request.getValueForKey("complaintTypeCode"))
					|| !locationId.equals(request.getValueForKey("boundaryId"))
					|| !assigneeId.equals(request.getAssignee().toString())) {
				isUpdate = true;
//			} else if ((!department.equals(request.getValueForKey("departmentName"))
			} else if((!status.equals(request.getStatus())
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
