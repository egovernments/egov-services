package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
public class PropertiesManager {

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

	@Value("${egov.services.tenant.host}")
	private String tenantServiceHostName;

	@Value("${egov.services.workflow_service.hostname.create_municipality_businesskey}")
	private String workflowServiceCreateMunicipalityBusinessKey;

	@Value("${egov.services.workflow_service.hostname.renew_municipality_businesskey}")
	private String workflowServiceRenewMunicipalityBusinessKey;
	
	@Value("${egov.services.workflow_service.hostname.cancel_municipality_businesskey}")
	private String workflowServiceCancelMunicipalityBusinessKey;

	@Value("${egov.services.workflow_service.hostname.evict_municipality_businesskey}")
	private String workflowServiceEvictMunicipalityBusinessKey;

	@Value("${egov.services.workflow_service.objection_municipality_businesskey}")
	private String workflowServiceObjectionMunicipalityBusinessKey;

	@Value("${egov.services.workflow_service.judgement_municipality_businesskey}")
	private String workflowServiceJudgementMunicipalityBusinessKey;

	@Value("${egov.services.workflow_service.hostname.create_corporation_businesskey}")
	private String workflowServiceCreateCorporationBusinessKey;

	@Value("${egov.services.workflow_service.hostname.renew_corporation_businesskey}")
	private String workflowServiceRenewCorporationBusinessKey;

	@Value("${egov.services.workflow_service.hostname.cancel_corporation_businesskey}")
	private String workflowServiceCancelCorporationBusinessKey;

	@Value("${egov.services.workflow_service.hostname.evict_corporation_businesskey}")
	private String workflowServiceEvictCorporationBusinessKey;

	@Value("${egov.services.workflow_service.objection_corporation_businesskey}")
	private String workflowServiceObjectionCorporationBusinessKey;

	@Value("${egov.services.workflow_service.judgement_corporation_businesskey}")
	private String workflowServiceJudgementCorporationBusinessKey;

	@Value("${kafka.topics.start.workflow}")
	private String kafkaStartWorkflowTopic;
	
	@Value("${kafka.topics.update.workflow}")
	private String kafkaUpdateworkflowTopic;
	
	@Value("${kafka.topics.save.agreement}")
	private String kafkaSaveAgreementTopic;
	
	@Value("${kafka.topics.update.agreement}")
	private String kafkaUpdateAgreementTopic;
	
	@Value("${egov.services.workflow_service.status.advancepaid}")
	private String wfSatusAdvancePaid;
	
	@Value("${egov.services.workflow_service.status.assistantapproved}")
	private String wfStatusAssistantApproved;
	
	@Value("${egov.services.workflow_service.agreement.status.rejected}")
	private String agreementStatusRejected;
	
	@Value("${egov.services.workflow_service.status.rejected}")
	private String wfStatusRejected;
	
	@Value("${egov.services.city.grade.corp}")
	private String cityGradeCorp;
	
}
