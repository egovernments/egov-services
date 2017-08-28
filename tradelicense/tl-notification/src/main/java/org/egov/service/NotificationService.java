package org.egov.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.notification.config.PropertiesManager;
import org.egov.notification.model.EmailMessage;
import org.egov.notification.model.EmailMessageContext;
import org.egov.notification.model.EmailRequest;
import org.egov.notification.model.SearchTenantResponse;
import org.egov.notification.model.SmsMessage;
import org.egov.notificationConsumer.NotificationUtil;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shubham
 *
 */

@Service
@Slf4j
public class NotificationService {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	NotificationUtil notificationUtil;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * This method is to send email and sms license acknowledgement
	 * 
	 * @param tradeLicenseRequest
	 */
	public void licenseNewCreationAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = tradeLicenseContract.getApplicationNumber();
			String ownerName = tradeLicenseContract.getOwnerName();
			String amount = "";
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = tradeLicenseContract.getApplicationDate();
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);
			propertyMessage.put("Application Fee", amount);
			propertyMessage.put("Application Date", applicationDate);
			propertyMessage.put("Link", propertiesManager.getLinkForEmailBody() + "/" + applicationNumber);

			String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseAcknowledgementSms(),
					propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext.setSubjectTemplateName(propertiesManager.getLicenseAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

			kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
			kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);

		}
	}

	public void licenseFeeCollectionAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = tradeLicenseContract.getApplicationNumber();
			String ownerName = tradeLicenseContract.getOwnerName();
			String amount = "";
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = tradeLicenseContract.getApplicationDate();
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);
			propertyMessage.put("Application Fee", amount);
			propertyMessage.put("Application Date", applicationDate);

			String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseFeeCollAcknowledgementSms(),
					propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseFeeCollAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext
					.setSubjectTemplateName(propertiesManager.getLicenseFeeCollAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		}
	}

	public void licenseAppForwardedAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = tradeLicenseContract.getApplicationNumber();
			String ownerName = tradeLicenseContract.getOwnerName();
			String amount = "";
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = tradeLicenseContract.getApplicationDate();
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);
			propertyMessage.put("Application Fee", amount);
			propertyMessage.put("Application Date", applicationDate);

			String message = notificationUtil
					.buildSmsMessage(propertiesManager.getLicenseAppForwordedAcknowledgementSms(), propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAppForwordedAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext
					.setSubjectTemplateName(propertiesManager.getLicenseAppForwordedAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		}
	}

	public void licenseAppApprovedAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = tradeLicenseContract.getApplicationNumber();
			String ownerName = tradeLicenseContract.getOwnerName();
			String amount = "";
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = tradeLicenseContract.getApplicationDate();
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);
			propertyMessage.put("Application Fee", amount);
			propertyMessage.put("Application Date", applicationDate);

			String message = notificationUtil
					.buildSmsMessage(propertiesManager.getLicenseAppApprovedAcknowledgementSms(), propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAppApprovedAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext
					.setSubjectTemplateName(propertiesManager.getLicenseAppApprovedAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		}
	}

	public void licenseCollectionORPaymentAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = tradeLicenseContract.getApplicationNumber();
			String ownerName = tradeLicenseContract.getOwnerName();
			String amount = "";
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = tradeLicenseContract.getApplicationDate();
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);
			propertyMessage.put("Application Fee", amount);
			propertyMessage.put("Application Date", applicationDate);

			String message = notificationUtil
					.buildSmsMessage(propertiesManager.getLicenseFeeCollPaymentAcknowledgementSms(), propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext
					.setBodyTemplateName(propertiesManager.getLicenseFeeCollPaymentAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext
					.setSubjectTemplateName(propertiesManager.getLicenseFeeCollPaymentAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		}
	}

	private String getULB(String tenantId,RequestInfo requestInfo) {

		String hostUrl = propertiesManager.getTenantServiceHostName() + propertiesManager.getTenantServiceBasePath();
		String searchUrl = propertiesManager.getTenantServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tenantId != null) {
			content.append("?code=" + tenantId);
		}

		url = url + content.toString();
		SearchTenantResponse tenantResponse = null;
		try {
			Map<String, Object> requestMap = new HashMap();
			requestMap.put("RequestInfo", requestInfo);
			tenantResponse = restTemplate.postForObject(url, requestInfo, SearchTenantResponse.class);

		} catch (Exception e) {
			log.debug("Error connecting to Tenant service end point " + url);
		}

		if (tenantResponse != null && tenantResponse.getTenant()!= null && tenantResponse.getTenant().size() != 0) {
			return tenantResponse.getTenant().get(0).getCity().getName();
		} else {
			return null;
		}

	}

}
