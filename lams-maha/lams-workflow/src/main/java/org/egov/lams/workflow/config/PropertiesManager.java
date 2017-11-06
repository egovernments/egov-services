package org.egov.lams.workflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
@Setter
@Getter
public class PropertiesManager {

	@Autowired
	Environment environment;
	
	@Value("${kafka.topic.start.workflow.agreement}")
	public String startAgreementWorkflowTopic;
	
	@Value("${kafka.topic.update.workflow.agreement}")
	public String updateAgreementWorkflowTopic;
	
	@Value("${kafka.topic.create.agreement}")
	public String createAgreementKafkaTopic;
	
	@Value("${kafka.topic.update.agreement}")
	public String updateAgreementKafkaTopic;
	
	@Value("${kafka.topic.save.estate}")
	public String createEstateKafkaTopic;
	
	@Value("${kafka.topic.update.estate}")
	public String updateEstateKafkaTopic;
	
	@Value("${kafka.topic.start.workflow.estate}")
	public String startEstateWorkflowTopic;
	
	@Value("${kafka.topic.update.workflow.estate}")
	public String updateEstateWorkflowTopic;
	
	@Value("${egov.services.workflow_service.hostname}")
	private String workflowServiceHostName;

	@Value("${egov.services.workflow_service.startpath}")
	private String workflowServiceStartPath;
	
	@Value("${egov.services.workflow_service.updatepath}")
	private String workflowServiceUpdatePath;
	
	@Value("${egov.services.workflow_service.searchpath}")
	private String workflowServiceSearchPath;
	
	@Value("${egov.services.workflow_service.taskpath}")
	private String workflowServiceTaskPAth;
	
	@Value("${egov.services.wf.businesskey}")
	private String workflowBusinessKey;
	
	@Value("${egov.services.workflow_service.status.rejected}")
	private String wfStatusRejected;
	
	@Value("${egov.services.workflow_service.status.assistantapproved}")
	private String wfStatusAssistantApproved;
}
