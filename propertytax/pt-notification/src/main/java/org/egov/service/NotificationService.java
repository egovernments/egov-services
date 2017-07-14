package org.egov.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.TaxCalculation;
import org.egov.models.User;
import org.egov.notification.model.EmailMessage;
import org.egov.notification.model.EmailMessageContext;
import org.egov.notification.model.EmailRequest;
import org.egov.notification.model.SmsMessage;
import org.egov.notification.model.TaxCalculationResponse;
import org.egov.notificationConsumer.NotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
/**
 * 
 * @author Yosadhara
 *
 */
public class NotificationService {

	@Autowired
	Environment environment;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	NotificationUtil notificationUtil;

	/**
	 * This method is to send email and sms Demand acknowledgement
	 * 
	 * @param propertyrequest
	 */
	public void demandAcknowledgement(PropertyRequest propertyrequest) {

		for (Property property : propertyrequest.getProperties()) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(environment.getProperty("demand.sms.acknowledgement"),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("demand.acknowledgement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(environment.getProperty("demand.acknowledgement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method to send email and sms for Demand approval
	 * 
	 * @param propertyrequest
	 */
	public void demandApprove(PropertyRequest propertyrequest) {

		for (Property property : propertyrequest.getProperties()) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());
			propertyMessage.put("landOwner", property.getPropertyDetail().getLandOwner());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(environment.getProperty("demand.sms.approve"),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(environment.getProperty("demand.approve.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(environment.getProperty("demand.approve.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * this method is to send email and sms to tansfer fee for demand
	 * 
	 * @param propertyrequest
	 */
	public void demandTransferFee(PropertyRequest propertyrequest) {

		for (Property property : propertyrequest.getProperties()) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(environment.getProperty("demand.sms.transferfee"),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(environment.getProperty("demand.transferfee.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(environment.getProperty("demand.transferfee.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Demand rejection
	 * 
	 * @param propertyrequest
	 */
	public void demandReject(PropertyRequest propertyrequest) {

		for (Property property : propertyrequest.getProperties()) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(environment.getProperty("demand.sms.reject"),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(environment.getProperty("demand.reject.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(environment.getProperty("demand.reject.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Property acknowledgement
	 * 
	 * @param properties
	 */
	public void propertyAcknowledgement(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(environment.getProperty("property.sms.acknowledgement"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("property.acknowledgement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("property.acknowledgement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Property approval
	 * 
	 * @param properties
	 */
	public void propertyApprove(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());
			// total Propertytax calculation logic
			String taxCalculations = property.getPropertyDetail().getTaxCalculations();
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			TaxCalculationResponse taxCalculationResponse = gson.fromJson(taxCalculations, TaxCalculationResponse.class);
			Double propertyTax = 0.0;
			for (TaxCalculation taxcalculation : taxCalculationResponse.getTaxCalculationList()) {
				propertyTax = propertyTax + taxcalculation.getPropertyTaxes().getTotalTax();
			}
			propertyMessage.put("propertyTax", propertyTax);
			propertyMessage.put("effectiveDate", property.getAssessmentDate());
			propertyMessage.put("municipalityName", property.getTenantId());
			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(environment.getProperty("property.sms.acknowledgement"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("property.acknowledgement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("property.acknowledgement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Property rejection
	 * 
	 * @param properties
	 */
	public void propertyReject(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(environment.getProperty("property.sms.acknowledgement"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("property.acknowledgement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("property.acknowledgement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for property revision petition
	 * acknowledgement
	 * 
	 * @param properties
	 */
	public void revisionPetitionAcknowldgement(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(
						environment.getProperty("revision.petition.acknowledgement.sms"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(
						environment.getProperty("revision.petition.acknowledgement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("revision.petition.acknowledgement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is for sending email and sms for property revision petition
	 * hearing
	 * 
	 * @param properties
	 */
	public void revisionPetitionHearing(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(environment.getProperty("revision.petition.hearing.sms"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("revision.petition.hearing.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("revision.petition.hearing.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}

	/**
	 * This method is for sending email and sms for property revision petition
	 * endorsement
	 * 
	 * @param properties
	 */
	public void revisionPetitionEndorsement(List<Property> properties) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			propertyMessage.put("assessmentDate", property.getAssessmentDate());
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(environment.getProperty("revision.petition.endorsement.sms"), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(environment.getProperty("revision.petition.endorsement.bodyTemplateName"));
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(
						environment.getProperty("revision.petition.endorsement.subjectTemplateName"));
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.sms"), smsMessage);
				kafkaTemplate.send(environment.getProperty("egov.propertytax.pt-notification.email"), emailMessage);
			}
		}
	}
}
