package org.egov.wcms.workflow.repository;

import org.egov.wcms.workflow.model.contract.ProcessInstanceRequest;
import org.egov.wcms.workflow.model.contract.ProcessInstanceResponse;
import org.egov.wcms.workflow.model.contract.TaskRequest;
import org.egov.wcms.workflow.model.contract.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepository {

	private RestTemplate restTemplate;
	private String startWorkflowUrl;
	private String updateWorkflowUrl;
	@Autowired
	public WorkflowRepository(@Value("${egov.services.workflow_service.hostname}") String commonWorkflowHostname,
			@Value("${egov.services.workflow_service.startpath}") String startPath,
			@Value("${egov.services.workflow_service.updatepath}") String updatePath, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		System.out.println( "commonWorkflowHostname= "+ commonWorkflowHostname + "startPath= "+startPath);
		this.startWorkflowUrl = commonWorkflowHostname + startPath;
		this.updateWorkflowUrl = commonWorkflowHostname + updatePath;
	}

	public ProcessInstanceResponse start(final ProcessInstanceRequest processInstanceRequest) {

	  final HttpEntity<ProcessInstanceRequest> request = new HttpEntity<>(processInstanceRequest);

		return restTemplate.postForObject(startWorkflowUrl, request, ProcessInstanceResponse.class);
	}

	public TaskResponse update(final TaskRequest taskRequest) {

		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

	        System.out.println("before call task update action= "+taskRequest.getTask().getAction() +" before call task update status="+taskRequest.getTask().getStatus());
		final HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest);

		return restTemplate.postForObject(updateWorkflowUrl.replaceAll("\\{id\\}", taskRequest.getTask().getId()),
				request, TaskResponse.class);
	}

}
