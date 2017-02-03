package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.entity.ProcessInstance;
import org.egov.workflow.entity.Task;
import org.egov.workflow.model.PositionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflowImpl implements WorkflowInterface {

	@Autowired
	private ComplaintRouterService complaintRouterService;

	@Override
	public ProcessInstance start(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasks(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance update(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task update(String jurisdiction, Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getHistoryDetail(String workflowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositionResponse getAssignee(Long boundaryId, String complaintTypeCode, Long assigneeId) {
		return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
	}

	@Override
	public List<Object> getAssignee(String deptCode, String designationName) {
		// TODO Auto-generated method stub
		return null;
	}

}
