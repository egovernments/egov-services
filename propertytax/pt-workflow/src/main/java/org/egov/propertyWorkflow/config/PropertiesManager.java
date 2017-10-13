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
}