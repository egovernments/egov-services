package org.egov.mr.service;

import org.egov.mr.contract.ProcessInstance;
import org.egov.mr.contract.TaskResponse;
import org.egov.mr.repository.WorkflowRepository;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MRWorkflowService {

	@Autowired
	private WorkflowRepository workflowRepository;

	public void startWorkflow(MarriageRegnRequest marriageRegnRequest) {
		log.info("MRWorkflowService startWorkflow marriageRegnRequest"+marriageRegnRequest);
		ProcessInstance processInstanceResponse = workflowRepository.startWorkflow(marriageRegnRequest);
		log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
	}

	public void updateWorkflow(MarriageRegnRequest marriageRegnRequest) {
		log.info("MRWorkflowService updateWorkflow marriageRegnRequest"+marriageRegnRequest);
		TaskResponse taskResponse = workflowRepository.updateWorkflow(marriageRegnRequest);
		log.info("the taskResponse from workflow update : " + taskResponse);
	}

}
