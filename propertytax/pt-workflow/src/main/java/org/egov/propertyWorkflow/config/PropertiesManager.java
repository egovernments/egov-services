package org.egov.propertyWorkflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Yosadhara
 *
 */
@Configuration
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@Service
public class PropertiesManager {
	@Autowired
	Environment environment;

	private String workflowHostName;

	private String workflowBasepath;

	private String workflowStartpath;

	private String workflowUpdatepath;

	private String approveProperty;

	private String upicNumberFormat;

	private String success;

	private String createTitleTransferUserValidator;

	private String updateTitleTransferUserValidator;

	private String createTitleTransferWorkflow;

	private String updateTitletransferWorkflow;

	private String approveTitletransfer;

	private String createPropertTaxCalculated;

	private String updatePropertTaxCalculated;

	private String taxGenerated;

	private String createWorkflow;

	private String updateWorkflow;

	private String approveWorkflow;

	private String businessKey;

	private String type;

	private String state;

	private String senderName;

	private String comment;

	private String action;

	private String bootstrapServer;

	private String autoOffsetReset;

	private String consumerGroupId;

	private String tenantHostName;

	private String tenantBasepath;

	private String tenantSearchpath;

	private String idName;

	private String idFormat;

	private String idHostName;

	private String idBasepath;

	private String idCreatepath;

	private String serverContextpath;

	private String userAuth;

	private String userActive;

	private String oauthName;

	private String password;

	private String grantType;

	private String Scope;

	private String tenantId;

	private String authKey;

	private String titileTransferBusinesskey;

	private String titleTransferType;

	private String titleTransferState;

	private String titleTransferComment;

	private String createTitleTransferTaxGenerated;

	private String updateTitleTransferTaxGenerated;

	private String reject;

	private String rejectProperty;

	private String titleTransferAcknowledgementTopic;

	private String titleTransferApproveTopic;

	private String titleTransferRejectTopic;

	private String modifyPropertTaxGenerated;

	private String updatePropertyTaxGenerated;

	private String modifyWorkflow;

	private String modifyaprroveWorkflow;

	private String specialNoticeAction;

	private String cancel;

	private String propertyHostName;

	private String propertyBasepath;

	private String propertySearch;

	private String createValidatedDemolition;
	
	private String updateValidatedDemolition;
	
	private String approveDemolitionWorkflow;
	
	private String createDemolitionWorkflow;
	
	private String rejectDemolition;
	
	private String updateDemolitionWorkflow;

	private String createValidatedTaxExemption;

	private String updateValidatedTaxExemption;

	private String taxExemptionCreateWorkflow;

	private String taxExemptionUpdateWorkflow;

	private String taxExemptionApproveWorkflow;

	private String taxExemptionBusinesskey;

	private String taxExemptionType;

	private String taxExemptionState;

	private String taxExemptionComment;

	private String rejectTaxExemption;

	private String approveTaxExemption;

	private String createValidatedVacancyRemission;

	private String updateValidatedVacancyRemission;

	private String vacancyRemissionCreateWorkflow;

	private String vacancyRemissionUpdateWorkflow;

	private String vacancyRemissionApproveWorkflow;
	
	private String vacancyRemissionBusinesskey;

	private String vacancyRemissionType;

	private String vacancyRemissionState;

	private String vacancyRemissionComment;
	
	private String rejectVacancyRemission;
	
	private String approveVacancyRemission;

	public String getWorkflowHostName() {
		return environment.getProperty("egov.services.egov-common-workflows.hostname");
	}

	public String getWorkflowBasepath() {
		return environment.getProperty("egov.services.egov-common-workflows.basepath");
	}

	public String getWorkflowStartpath() {
		return environment.getProperty("egov.services.egov-common-workflows.startpath");
	}

	public String getWorkflowUpdatepath() {
		return environment.getProperty("egov.services.egov-common-workflows.updatepath");
	}

	public String getApproveProperty() {
		return environment.getProperty("property.approved");
	}

	public String getUpicNumberFormat() {
		return environment.getProperty("upic.number.format");
	}

	public String getSuccess() {
		return environment.getProperty("success");
	}

	public String getCreateTitleTransferUserValidator() {
		return environment.getProperty("egov.propertytax.property.titletransfer.user.validator");
	}

	public String getUpdateTitleTransferUserValidator() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.user.validator");
	}

	public String getApproveTitletransfer() {
		return environment.getProperty("egov.propertytax.property.titletransfer.approved");
	}

	public String getCreatePropertTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.tax.calculated");
	}

	public String getUpdatePropertTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.update.tax.calculated");
	}

	public String getTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.tax.generated");
	}

	public String getCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.workflow.started");
	}

	public String getUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.workflow.started");
	}

	public String getApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.workflow.approved");
	}

	public String getBusinessKey() {
		return environment.getProperty("businessKey");
	}

	public String getType() {
		return environment.getProperty("type");
	}

	public String getState() {
		return environment.getProperty("state");
	}

	public String getSenderName() {
		return environment.getProperty("senderName");
	}

	public String getComment() {
		return environment.getProperty("create.property.comments");
	}

	public String getAction() {
		return environment.getProperty("action");
	}

	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("auto.offset.reset");
	}

	public String getConsumerGroupId() {
		return environment.getProperty("consumer.groupId");
	}

	public String getTenantHostName() {
		return environment.getProperty("egov.services.tenant.hostname");
	}

	public String getTenantBasepath() {
		return environment.getProperty("egov.services.tenant.basepath");
	}

	public String getTenantSearchpath() {
		return environment.getProperty("egov.services.tenant.searchpath");
	}

	public String getIdName() {
		return environment.getProperty("id.idName");
	}

	public String getIdFormat() {
		return environment.getProperty("id.format");
	}

	public String getIdHostName() {
		return environment.getProperty("egov.services.egov_idgen.hostname");
	}

	public String getIdBasepath() {
		return environment.getProperty("egov.services.egov_idgen.basepath");
	}

	public String getIdCreatepath() {
		return environment.getProperty("egov.services.egov_idgen.createpath");
	}

	public String getServerContextpath() {
		return environment.getProperty("server.context-path");
	}

	public String getUserAuth() {
		return environment.getProperty("user.auth");
	}

	public String getUserActive() {
		return environment.getProperty("user.active");
	}

	public String getOauthName() {
		return environment.getProperty("oauth.username");
	}

	public String getPassword() {
		return environment.getProperty("password");
	}

	public String getGrantType() {
		return environment.getProperty("grant_type");
	}

	public String getScope() {
		return environment.getProperty("scope");
	}

	public String getTenantId() {
		return environment.getProperty("tenantId");
	}

	public String getAuthKey() {
		return environment.getProperty("authkey");
	}

	public String getTitileTransferBusinesskey() {
		return environment.getProperty("titletransfer.businesskey");
	}

	public String getTitleTransferType() {
		return environment.getProperty("titletransfer.type");
	}

	public String getTitleTransferState() {
		return environment.getProperty("titletransfer.state");
	}

	public String getTitleTransferComment() {
		return environment.getProperty("titletransfer.comment");
	}

	public String getCreateTitleTransferTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.tax.generated");
	}

	public String getUpdateTitleTransferTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.tax.generated");
	}

	public String getCreateTitleTransferWorkflow() {
		return environment.getProperty("egov.propertytax.property.titletransfer.workflow");
	}

	public String getUpdateTitletransferWorkflow() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.workflow");
	}

	public String getReject() {
		return environment.getProperty("reject");
	}

	public String getRejectProperty() {

		return environment.getProperty("egov.propertytax.property.rejected");
	}

	public String getTitleTransferAcknowledgementTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.title.transfer.validated");
	}

	public String getTitleTransferApproveTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.title.transfer.approve");
	}

	public String getTitleTransferRejectTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.title.transfer.reject");
	}

	public String getModifyPropertTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.modify.tax.generated");
	}

	public String getModifyWorkflow() {
		return environment.getProperty("egov.propertytax.property.modify.workflow.started");
	}

	public String getModifyaprroveWorkflow() {
		return environment.getProperty("egov.propertytax.property.modify.workflow.approved");
	}

	public String getUpdatePropertyTaxGenerated() {
		return environment.getProperty("egov.propertytax.property.update.tax.calculated");
	}

	public String getSpecialNoticeAction() {
		return environment.getProperty("special.notice.action");
	}

	public String getCancel() {
		return environment.getProperty("cancel");
	}

	public String getPropertyHostName() {
		return environment.getProperty("egov.services.pt_property.hostname");
	}

	public String getPropertyBasepath() {
		return environment.getProperty("egov.services.pt_property.basepath");
	}

	public String getPropertySearch() {
		return environment.getProperty("egov.services.pt_property.search");
	}
	
	public String getCreateValidatedDemolition() {
		return environment.getProperty("egov.propertytax.create.demolition.validated");
	}

	public String getUpdateValidatedDemolition() {
		return environment.getProperty("egov.propertytax.update.demolition.validated");
	}

	public String getApproveDemolitionWorkflow() {
		return environment.getProperty("egov.propertytax.demolition.approved");
	}

	public String getCreateDemolitionWorkflow() {
		return environment.getProperty("egov.propertytax.create.demolition.workflow.started");
	}

	public String getRejectDemolition() {
		return environment.getProperty("egov.propertytax.demolition.rejected");
	}

	public String getUpdateDemolitionWorkflow() {
		return environment.getProperty("egov.propertytax.demolition.update.workflow.started");
	}

	public String getCreateValidatedVacancyRemission() {
		return environment.getProperty("egov.propertytax.property.vacancyremission.create.validated");
	}

	public String getUpdateValidatedVacancyRemission() {
		return environment.getProperty("egov.propertytax.property.vacancyremission.update.validated");
	}

	public String getVacancyRemissionCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.vacancyremission.workflow.started");
	}

	public String getVacancyRemissionUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.vacancyremission.workflow.started");
	}

	public String getCreateValidatedTaxExemption() {
		return environment.getProperty("egov.propertytax.property.taxexemption.create.validated");
	}

	public String getUpdateValidatedTaxExemption() {
		return environment.getProperty("egov.propertytax.property.taxexemption.update.validated");
	}

	public String getTaxExemptionCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.taxexemption.workflow.started");
	}

	public String getTaxExemptionUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.taxexemption.workflow.started");
	}

	public String getTaxExemptionApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.taxexemption.workflow.approved");
	}

	public String getRejectTaxExemption() {
		return environment.getProperty("egov.propertytax.property.taxexemption.workflow.rejected");
	}

	public String getTaxExemptionBusinesskey() {
		return environment.getProperty("taxexemption.businesskey");
	}

	public String getTaxExemptionType() {
		return environment.getProperty("taxexemption.type");
	}

	public String getTaxExemptionState() {
		return environment.getProperty("taxexemption.state");
	}

	public String getTaxExemptionComment() {
		return environment.getProperty("taxexemption.comment");
	}

	public String getApproveTaxExemption() {
		return environment.getProperty("taxexemption.approved");
	}

	public String getVacancyRemissionApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.vacancyremission.workflow.approved");
	}
	
	public String getVacancyRemissionBusinesskey() {
		return environment.getProperty("vacancyremission.businesskey");
	}

	public String getVacancyRemissionType() {
		return environment.getProperty("vacancyremission.type");
	}

	public String getVacancyRemissionState() {
		return environment.getProperty("vacancyremission.state");
	}

	public String getVacancyRemissionComment() {
		return environment.getProperty("vacancyremission.comment");
	}	
	public String getRejectVacancyRemission() {
		return environment.getProperty("egov.propertytax.property.vacancyremission.workflow.rejected");
	}
	public String getApproveVacancyRemission() {
		return environment.getProperty("vacancyremission.approved");
	}
}