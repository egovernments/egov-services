package org.egov.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Yosadhara
 *
 */
@Configuration
@ToString
@Getter
@NoArgsConstructor
@SuppressWarnings("unused")
@Service
public class PropertiesManager {

	@Autowired
	Environment environment;

	private String templateType;

	private String templateFolder;

	private String templatePriority;

	private String bootstrapServer;

	private String autoOffsetReset;

	private String groupName;

	private String emailNotification;

	private String smsNotification;

	private String demandAcknowledgement;

	private String demandApprove;

	private String demandTransferfee;

	private String demandReject;

	private String propertyAcknowledgement;

	private String approveProperty;

	private String rejectProperty;

	private String propertyAlterationAcknowledgement;

	private String approvePropertyAlteration;

	private String rejectPropertyAlteration;

	private String revisionPetitionAcknowledgement;

	private String revisionPetitionHearing;

	private String revisionPetitionEndorsement;

	private String demandAcknowledgementEmailBody;

	private String demandAcknowledgementEmailSubject;

	private String demandApproveEmailBody;

	private String demandApproveEmailSubject;

	private String demandTransferfeeEmailBody;

	private String demandTransferfeeEmailSubject;

	private String demandRejectEmailBody;

	private String demandRejectEmailSubject;

	private String demandAcknowledgementSms;

	private String demandApproveSms;

	private String demandTransferfeeSms;

	private String demandRejectSms;

	private String propertyAcknowledgementEmailBody;

	private String propertyAcknowledgementEmailSubject;

	private String propertyApproveEmailBody;

	private String propertyApproveEmailSubject;

	private String propertyRejectEmailBody;

	private String propertyRejectEmailSubject;

	private String propertyAcknowledgementSms;

	private String propertyApproveSms;

	private String propertyRejectSms;

	private String alterationAcknowledgementEmailBody;

	private String alterationAcknowledgementEmailSubject;

	private String alterationApproveEmailBody;

	private String alterationApproveEmailSubject;

	private String alterationRejectEmailBody;

	private String alterationRejectEmailSubject;

	private String alterationAcknowledgementSms;

	private String alterationApproveSms;

	private String alterationRejectSms;

	private String revisionPetitionAcknowledgementEmailSubject;

	private String revisionPetitionAcknowledgementEmailBody;

	private String revisionPetitionHearingEmailSubject;

	private String revisionPetitionHearingEmailBody;

	private String revisionPetitionEndorsementEmailSubject;

	private String revisionPetitionEndorsementEmailBody;

	private String revisionPetitionAcknowledgementSms;

	private String revisionPetitionHearingSms;

	private String revisionPetitionEndorsementSms;

	private String serverContextpath;

	private String billingServiceHostname;

	private String billingServiceSearchdemand;

	private String businessService;
	
	// TITLE TRANSFER CONSUMER, EMAIL, and SMS Template
	private String titleTransferAcknowledgementTopic;

	private String titleTransferApproveTopic;

	private String titleTransferRejectTopic;

	private String titleTransferAcknowledgementEmailSubject;

	private String titleTransferAcknowledgementEmailBody;

	private String titleTransferApproveEmailSubject;

	private String titleTransferApproveEmailBody;

	private String titleTransferRejectEmailSubject;

	private String titleTransferRejectEmailBody;

	private String titleTransferAcknowledgementSms;

	private String titleTransferApproveSms;

	private String titleTransferRejectSms;

	// VACANCY REMISSION CONSUMER, EMAIL, and SMS Template
	private String vacancyRemissionAcknowledgementTopic;

	private String vacancyRemissionApproveTopic;

	private String vacancyRemissionRejectTopic;

	private String vacancyRemissionsAcknowledgementEmailSubject;

	private String vacancyRemissionsAcknowledgementEmailBody;

	private String vacancyRemissionsApproveEmailSubject;

	private String vacancyRemissionsApproveEmailBody;

	private String vacancyRemissionsRejectEmailSubject;

	private String vacancyRemissionsRejectEmailBody;

	private String vacancyRemissionsAcknowledgementSms;

	private String vacancyRemissionsApproveSms;

	private String vacancyRemissionsRejectSms;
	
	private String titleTransferRejectComment;
	
	private String propertyHostName;
	
	private String propertyBasePath;
	
	private String searchProperty;
	
	private String searchNotice;
	
	private String approvalOrRejectionComment;
	
	private String noticePath;
	
	private String egovServicesHost;
	
	private String rejectionDownloadPath;
	
	private String rejectionLetter;
	
	private String cancel;
	
	private String propertyRejectCancelEmailBody;

	public String getTemplateType() {
		return environment.getProperty("pt-notification.template.type");
	}

	public String getTemplateFolder() {
		return environment.getProperty("pt-notification.template.folder");
	}

	public String getTemplatePriority() {
		return environment.getProperty("pt-notification.template.priority");
	}

	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("pt-notification.auto.offset.reset.config");
	}

	public String getGroupName() {
		return environment.getProperty("pt-notification.groupName");
	}

	public String getEmailNotification() {
		return environment.getProperty("egov.propertytax.pt-notification.email");
	}

	public String getSmsNotification() {
		return environment.getProperty("egov.propertytax.pt-notification.sms");
	}

	public String getDemandAcknowledgement() {
		return environment.getProperty("egov.propertytax.pt-notification.demand.validated");
	}

	public String getDemandApprove() {
		return environment.getProperty("egov.propertytax.pt-notification.demand.approve");
	}

	public String getDemandTransferfee() {
		return environment.getProperty("egov.propertytax.pt-notification.demand.transferfee");
	}

	public String getDemandReject() {
		return environment.getProperty("egov.propertytax.pt-notification.demand.reject");
	}

	public String getPropertyAcknowledgement() {
		return environment.getProperty("egov.propertytax.pt-notification.property.validated");
	}

	public String getApproveProperty() {
		return environment.getProperty("egov.propertytax.pt-notification.property.approve");
	}

	public String getRejectProperty() {
		return environment.getProperty("egov.propertytax.pt-notification.property.reject");
	}

	public String getPropertyAlterationAcknowledgement() {
		return environment.getProperty("egov.propertytax.pt-notification.property.alteration.validated");
	}

	public String getApprovePropertyAlteration() {
		return environment.getProperty("egov.propertytax.pt-notification.property.alteration.approve");
	}

	public String getRejectPropertyAlteration() {
		return environment.getProperty("egov.propertytax.pt-notification.property.alteration.reject");
	}

	public String getRevisionPetitionAcknowledgement() {
		return environment.getProperty("egov.propertytax.pt-notification.revision.petition.validated");
	}

	public String getRevisionPetitionHearing() {
		return environment.getProperty("egov.propertytax.pt-notification.revision.petition.hearing");
	}

	public String getRevisionPetitionEndorsement() {
		return environment.getProperty("egov.propertytax.pt-notification.revision.petition.endorsement");
	}

	public String getDemandAcknowledgementEmailBody() {
		return environment.getProperty("demand.acknowledgement.bodyTemplateName");
	}

	public String getDemandAcknowledgementEmailSubject() {
		return environment.getProperty("demand.acknowledgement.subjectTemplateName");
	}

	public String getDemandApproveEmailBody() {
		return environment.getProperty("demand.approve.bodyTemplateName");
	}

	public String getDemandApproveEmailSubject() {
		return environment.getProperty("demand.approve.subjectTemplateName");
	}

	public String getDemandTransferfeeEmailBody() {
		return environment.getProperty("demand.transferfee.bodyTemplateName");
	}

	public String getDemandTransferfeeEmailSubject() {
		return environment.getProperty("demand.transferfee.subjectTemplateName");
	}

	public String getDemandRejectEmailBody() {
		return environment.getProperty("demand.reject.bodyTemplateName");
	}

	public String getDemandRejectEmailSubject() {
		return environment.getProperty("demand.reject.subjectTemplateName");
	}

	public String getDemandAcknowledgementSms() {
		return environment.getProperty("demand.sms.acknowledgement");
	}

	public String getDemandApproveSms() {
		return environment.getProperty("demand.sms.approve");
	}

	public String getDemandTransferfeeSms() {
		return environment.getProperty("demand.sms.transferfee");
	}

	public String getDemandRejectSms() {
		return environment.getProperty("demand.sms.reject");
	}

	public String getPropertyAcknowledgementEmailBody() {
		return environment.getProperty("property.acknowledgement.bodyTemplateName");
	}

	public String getPropertyAcknowledgementEmailSubject() {
		return environment.getProperty("property.acknowledgement.subjectTemplateName");
	}

	public String getPropertyApproveEmailBody() {
		return environment.getProperty("property.approve.bodyTemplateName");
	}

	public String getPropertyApproveEmailSubject() {
		return environment.getProperty("property.approve.subjectTemplateName");
	}

	public String getPropertyRejectEmailBody() {
		return environment.getProperty("property.reject.bodyTemplateName");
	}

	public String getPropertyRejectEmailSubject() {
		return environment.getProperty("property.reject.subjectTemplateName");
	}

	public String getPropertyAcknowledgementSms() {
		return environment.getProperty("property.sms.acknowledgement");
	}

	public String getPropertyApproveSms() {
		return environment.getProperty("property.sms.approve");
	}

	public String getPropertyRejectSms() {
		return environment.getProperty("property.sms.reject");
	}

	public String getAlterationAcknowledgementEmailBody() {
		return environment.getProperty("property.alteration.acknowledgement.bodyTemplateName");
	}

	public String getAlterationAcknowledgementEmailSubject() {
		return environment.getProperty("property.alteration.acknowledgement.subjectTemplateName");
	}

	public String getAlterationApproveEmailBody() {
		return environment.getProperty("property.alteration.approve.bodyTemplateName");
	}

	public String getAlterationApproveEmailSubject() {
		return environment.getProperty("property.alteration.approve.subjectTemplateName");
	}

	public String getAlterationRejectEmailBody() {
		return environment.getProperty("property.alteration.reject.bodyTemplateName");
	}

	public String getAlterationRejectEmailSubject() {
		return environment.getProperty("property.alteration.reject.subjectTemplateName");
	}

	public String getAlterationAcknowledgementSms() {
		return environment.getProperty("property.alteration.acknowledgement.sms");
	}

	public String getAlterationApproveSms() {
		return environment.getProperty("property.alteration.approve.sms");
	}

	public String getAlterationRejectSms() {
		return environment.getProperty("property.alteration.reject.sms");
	}

	public String getRevisionPetitionAcknowledgementEmailSubject() {
		return environment.getProperty("revision.petition.acknowledgement.subjectTemplateName");
	}

	public String getRevisionPetitionAcknowledgementEmailBody() {
		return environment.getProperty("revision.petition.acknowledgement.bodyTemplateName");
	}

	public String getRevisionPetitionHearingEmailSubject() {
		return environment.getProperty("revision.petition.hearing.subjectTemplateName");
	}

	public String getRevisionPetitionHearingEmailBody() {
		return environment.getProperty("revision.petition.hearing.bodyTemplateName");
	}

	public String getRevisionPetitionEndorsementEmailSubject() {
		return environment.getProperty("revision.petition.endorsement.subjectTemplateName");
	}

	public String getRevisionPetitionEndorsementEmailBody() {
		return environment.getProperty("revision.petition.endorsement.bodyTemplateName");
	}

	public String getRevisionPetitionAcknowledgementSms() {
		return environment.getProperty("revision.petition.acknowledgement.sms");
	}

	public String getRevisionPetitionHearingSms() {
		return environment.getProperty("revision.petition.hearing.sms");
	}

	public String getRevisionPetitionEndorsementSms() {
		return environment.getProperty("revision.petition.endorsement.sms");
	}

	public String getServerContextpath() {
		return environment.getProperty("server.contextpath");
	}

	public String getBillingServiceHostname() {
		return environment.getProperty("egov.services.billing_service.hostname");
	}

	public String getBillingServiceSearchdemand() {
		return environment.getProperty("egov.services.billing_service.searchdemand");
	}

	public String getBusinessService() {
		return environment.getProperty("business.service");
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
	
	public String getTitleTransferAcknowledgementEmailSubject() {
		return environment.getProperty("title.transfer.acknowledgement.subjectTemplate");
	}

	public String getTitleTransferAcknowledgementEmailBody() {
		return environment.getProperty("title.transfer.acknowledgement.bodyTemplate");
	}

	public String getTitleTransferApproveEmailSubject() {
		return environment.getProperty("title.transfer.approve.subjectTemplate");
	}

	public String getTitleTransferApproveEmailBody() {
		return environment.getProperty("title.transfer.approve.bodyTemplate");
	}

	public String getTitleTransferRejectEmailSubject() {
		return environment.getProperty("title.transfer.reject.subjectTemplate");
	}

	public String getTitleTransferRejectEmailBody() {
		return environment.getProperty("title.transfer.reject.boytTemplate");
	}

	public String getTitleTransferAcknowledgementSms() {
		return environment.getProperty("title.transfer.acknowledgement.sms");
	}
		
	public String getTitleTransferApproveSms() {
		return environment.getProperty("title.transfer.approve.sms");
	}

	public String getTitleTransferRejectSms() {
		return environment.getProperty("title.transfer.reject.sms");
	}
	
	public String getVacancyRemissionAcknowledgementTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.vacancy.remissions.validated");
	}

	public String getVacancyRemissionApproveTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.vacancy.remissions.approve");
	}

	public String getVacancyRemissionRejectTopic() {
		return environment.getProperty("egov.propertytax.pt-notification.vacancy.remissions.reject");
	}

	public String getVacancyRemissionsAcknowledgementEmailSubject() {
		return environment.getProperty("vacancy.remissions.acknowledgement.subjectTemplate");
	}

	public String getVacancyRemissionsAcknowledgementEmailBody() {
		return environment.getProperty("vacancy.remissions.acknowledgement.bodyTemplate");
	}

	public String getVacancyRemissionsApproveEmailSubject() {
		return environment.getProperty("vacancy.remissions.approve.subjectTemplate");
	}

	public String getVacancyRemissionsApproveEmailBody() {
		return environment.getProperty("vacancy.remissions.approve.bodyTemplate");
	}

	public String getVacancyRemissionsRejectEmailSubject() {
		return environment.getProperty("vacancy.remissions.reject.subjectTemplate");
	}

	public String getVacancyRemissionsRejectEmailBody() {
		return environment.getProperty("vacancy.remissions.reject.boytTemplate");
	}

	public String getVacancyRemissionsAcknowledgementSms() {
		return environment.getProperty("vacancy.remissions.acknowledgement.sms");
	}

	public String getVacancyRemissionsApproveSms() {
		return environment.getProperty("vacancy.remissions.approve.sms");
	}

	public String getVacancyRemissionsRejectSms() {
		return environment.getProperty("vacancy.remissions.reject.sms");
	}
	
	public String getTitleTransferRejectComment() {
		return environment.getProperty("title.transfer.reject.comment");
	}
	public String getPropertyHostName() {
		return environment.getProperty("egov.services.pt_property.hostname");
	}
	public String getPropertyBasePath() {
		return environment.getProperty("egov.services.pt_property.basepath");
	}
	public String getSearchProperty() {
		return environment.getProperty("egov.services.pt_property.search.property");
	}
	public String getSearchNotice() {
		return environment.getProperty("egov.services.pt_property.search.notice");
	}	
	public String getApprovalOrRejectionComment() {
		return environment.getProperty("approval.or.rejection.comment");
	}
	public String getNoticePath() {
		return environment.getProperty("egov.services.pt_property.noticepath");
	}
	public String getEgovServicesHost() {
		return environment.getProperty("egov.services.fqdn.name");
	}	
	public String getRejectionDownloadPath() {
		return environment.getProperty("egov.file_store.rejection.downloadpath");
	}
	public String getRejectionLetter() {
		return environment.getProperty("rejection.letter");
	}
	public String getCancel() {
		return environment.getProperty("cancel");
	}
	public String getPropertyRejectCancelEmailBody() {
		return environment.getProperty("property.reject.cancel.bodyTemplateName");
	}
}