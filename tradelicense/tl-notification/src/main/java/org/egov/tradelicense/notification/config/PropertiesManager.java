package org.egov.tradelicense.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${app.timezone}")
	private String appTimeZone;

	@Value("${error.license.licenses.notfound}")
	private String tradeLicensesNotFoundMsg;

	@Value("${tl-notification.template.priority}")
	private String templatePriority;

	@Value("${tl-notification.template.folder}")
	private String templateFolder;

	@Value("${tl-notification.template.type}")
	private String templateType;

	// kafka topics and keys
	@Value("${egov.services.tl-services.tradelicense.validated.topic}")
	private String tradeLicenseValidatedTopic;

	@Value("${license.sms.acknowledgement}")
	private String licenseAcknowledgementSms;

	@Value("${license.email.body.acknowledgement}")
	private String licenseAcknowledgementEmailBody;

	@Value("${license.email.subject.acknowledgement}")
	private String licenseAcknowledgementEmailSubject;

	@Value("${egov.tradelicense.tl-notification.sms}")
	private String smsNotification;

	@Value("${egov.tradelicense.tl-notification.email}")
	private String emailNotification;

	@Value("${license.fee.collection.sms.acknowledgement}")
	private String licenseFeeCollAcknowledgementSms;

	@Value("${license.fee.collection.email.body.acknowledgement}")
	private String licenseFeeCollAcknowledgementEmailBody;

	@Value("${license.fee.collection.email.subject.acknowledgement}")
	private String licenseFeeCollAcknowledgementEmailSubject;

	@Value("${tradelicense.app.forwarded.acknowledgement.sms}")
	private String licenseAppForwordedAcknowledgementSms;

	@Value("${tradelicense.app.forwarded.acknowledgement.email.body}")
	private String licenseAppForwordedAcknowledgementEmailBody;

	@Value("${tradelicense.app.forwarded.acknowledgement.email.subject}")
	private String licenseAppForwordedAcknowledgementEmailSubject;

	@Value("${tradelicense.app.approved.acknowledgement.sms}")
	private String licenseAppApprovedAcknowledgementSms;

	@Value("${tradelicense.app.approved.acknowledgement.email.body}")
	private String licenseAppApprovedAcknowledgementEmailBody;

	@Value("${tradelicense.app.approved.acknowledgement.email.subject}")
	private String licenseAppApprovedAcknowledgementEmailSubject;
	
	@Value("${tradelicense.app.rejection.acknowledgement.sms}")
	private String licenseAppRejectionAcknowledgementSms;

	@Value("${tradelicense.app.rejection.acknowledgement.email.body}")
	private String licenseAppRejectionAcknowledgementEmailBody;

	@Value("${tradelicense.app.rejection.acknowledgement.email.subject}")
	private String licenseAppRejectionAcknowledgementEmailSubject;

	@Value("${tradelicense.collection.payment.acknowledgement.sms}")
	private String licenseFeeCollPaymentAcknowledgementSms;

	@Value("${tradelicense.collection.payment.acknowledgement.email.body}")
	private String licenseFeeCollPaymentAcknowledgementEmailBody;

	@Value("${tradelicense.collection.payment.acknowledgement.email.subject}")
	private String licenseFeeCollPaymentAcknowledgementEmailSubject;

	@Value("${egov.services.tenant.service.hostname}")
	private String tenantServiceHostName;

	@Value("${egov.services.tenant.service.basepath}")
	private String tenantServiceBasePath;

	@Value("${egov.services.tenant.service.searchpath}")
	private String tenantServiceSearchPath;
	
	@Value("${egov.services.tl-masters_v1.hostname}")
	private String tradeLicenseMasterServiceHostName;

	@Value("${egov.services.tl-masters_v1.basepath}")
	private String tradeLicenseMasterServiceBasePath;
	
	@Value("${egov.services.tl-masters_v1.status.searchpath}")
	private String statusServiceSearchPath;
	
	@Value("${egov.services.tl-services.tradelicense.persisted.topic}")
	private String tradeLicensePersistedTopic;
	
	@Value("${egov.services.tl-services_v1.hostname}")
	private String tradeLicenseServicesHostName;
	
	@Value("${egov.services.tl-services_v1.basepath}")
	private String tradeLicenseServicesBasePath;
	
	@Value("${egov.services.tl-services_v1.noticedocument.searchpath}")
	private String noticeDocumentServiceSearchPath;
	
	@Value("${egov.services.fqdn.name}")
	private String egovServicesHost;
	
	@Value("${egov.services.rejection.downloadpath}")
	private String rejectionDownloadPath;
	
	@Value("${egov.services.collections.collection-services.hostname}")
	private String collectionServiceHostName;
	
	@Value("${egov.services.collections.collection-services.basepath}")
	private String collectionServiceBasePath;
	
	@Value("${egov.services.collections.collection-services.searchpath}")
	private String collectionSearchPath;

}