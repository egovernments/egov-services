package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.CommonWorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.ProcessInstanceResponse;
import org.egov.pgr.employee.enrichment.repository.contract.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonWorkflowService {

	private CommonWorkflowRepository commonWorkflowRepository;

	@Autowired
	public CommonWorkflowService(CommonWorkflowRepository commonWorkflowRepository) {
		this.commonWorkflowRepository = commonWorkflowRepository;
	}

	public SevaRequest enrichWorkflow(SevaRequest sevaRequest) {

		if (sevaRequest.isWorkflowCreate() && sevaRequest.isCreate()) {

			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();

			processInstanceResponse = commonWorkflowRepository.start(sevaRequest.getProcessInstanceRequest());

			if (processInstanceResponse != null)
				sevaRequest.update(processInstanceResponse);

		} else {

			TaskResponse taskResponse = new TaskResponse();

			taskResponse = commonWorkflowRepository.update(sevaRequest.getTaskRequest());

			if (taskResponse != null)
				sevaRequest.update(taskResponse);

		}

		return sevaRequest;
	}

}
