package org.egov.notification.config;

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

	@Value("${tradelicense.coll.payment.acknowledgement.sms}")
	private String licenseFeeCollPaymentAcknowledgementSms;

	@Value("${tradelicense.coll.payment.acknowledgement.email.body}")
	private String licenseFeeCollPaymentAcknowledgementEmailBody;

	@Value("${tradelicense.coll.payment.acknowledgement.email.subject}")
	private String licenseFeeCollPaymentAcknowledgementEmailSubject;

	@Value("${egov.services.tenant.service.hostname}")
	private String tenantServiceHostName;

	@Value("${egov.services.tenant.service.basepath}")
	private String tenantServiceBasePath;

	@Value("${egov.services.tenant.service.searchpath}")
	private String tenantServiceSearchPath;

}