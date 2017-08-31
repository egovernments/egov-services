package org.egov.mr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
public class PropertiesManager {

	@Value("${egov.services.wf.businesskey}")
	private String workflowServiceBusinessKey;
	
	@Value("${egov.services.workflow_service.hostname}")
	private String workflowServiceHostName;

	@Value("${egov.services.workflow_service.startpath}")
	private String workflowServiceStartPath;
	
	@Value("${kafka.topics.create.workflow}")
	private String createWorkflowTopicName;
	
	@Value("${kafka.topics.create.registrationunit}")
	private String createRegistrationUnitTopicName;

	@Value("${kafka.topics.update.registrationunit}")
	private String updateRegistrationUnitTopicName;
	
	// kafka key access
	@Value("${kafka.key.registrationunit}")
	private String registrationUnitKey;
	
	@Value("${kafka.topics.create.marriageregn}")
	private String createMarriageRegnTopicName;

	@Value("${kafka.topics.update.marriageregn}")
	private String updateMarriageRegnTopicName;
	
	@Value("${kafka.topics.update.workflow}")
	private String kafkaUpdateworkflowTopic;
	
	// kafka key access
	@Value("${kafka.key.marriageregn}")
	private String marriageRegnKey;
	
	@Value("${egov.services.workflow_service.updatepath}")
	private String workflowServiceUpdatePath;
	
	@Value("${egov.services.workflow_service.taskpath}")
	private String workflowServiceTaskPAth;
	
	@Value("${egov.services.workflow_service.searchpath}")
	private String workflowServiceSearchPath;
	
	@Value("${egov.services.workflow_service.status.advancepaid}")
	private String wfSatusAdvancePaid;
	
	@Value("${egov.services.workflow_service.status.assistantapproved}")
	private String wfStatusAssistantApproved;
	
	@Value("${egov.services.workflow_service.status.rejected}")
	private String wfStatusRejected;
}
