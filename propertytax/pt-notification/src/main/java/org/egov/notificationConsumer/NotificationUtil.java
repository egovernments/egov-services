package org.egov.notificationConsumer;

import java.util.Map;

import org.egov.notification.model.EmailMessage;
import org.egov.notification.model.EmailMessageContext;
import org.egov.notification.model.EmailRequest;
import org.egov.notification.model.SMSMessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/**
 * 
 * @author Yosadhara
 *
 */
public class NotificationUtil {

	@Autowired
	TemplateUtil templateService;

	/**
	 * 
	 * @param messageType
	 * @param messageParameters
	 * @return {@link String}
	 */
	public String buildSmsMessage(String messageType, Map<Object, Object> messageParameters) {

		String smsMessage = getSMSMessage(messageType, messageParameters);

		return smsMessage;
	}

	/**
	 * 
	 * @param messageType
	 * @param messageParameters
	 * @return {@link String}
	 */
	private String getSMSMessage(String messageType, Map<Object, Object> messageParameters) {

		SMSMessageContext messageContext = new SMSMessageContext();

		messageContext.setTemplateName(messageType);
		messageContext.setTemplateValues(messageParameters);

		return templateService.loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
	}

	/**
	 * 
	 * @param emailRequest
	 * @param emailAddress
	 * @return {@link EmailMessage}
	 */
	public EmailMessage buildEmailTemplate(EmailRequest emailRequest, String emailAddress) {

		EmailMessage emailMessage = EmailMessage.builder().body(emailRequest.getBody())
				.subject(emailRequest.getSubject()).sender("").email(emailAddress).build();

		return emailMessage;
	}
	
	public EmailMessage buildRejectionEmailTemplate(EmailMessageContext emailMessageContext, String emailAddress) {

		return EmailMessage.builder().subject(getEmailSubject(emailMessageContext))
				.body(getMailBody(emailMessageContext)).email(emailAddress).isHTML(true).build();
	}

	/**
	 * 
	 * @param messageContext
	 * @return {@link EmailRequest}
	 */
	public EmailRequest getEmailRequest(EmailMessageContext messageContext) {

		return EmailRequest.builder().subject(getEmailSubject(messageContext)).body(getMailBody(messageContext))
				.build();
	}

	/**
	 * 
	 * @param messageContext
	 * @return {@link String}
	 */
	private String getEmailSubject(EmailMessageContext messageContext) {

		return templateService.loadByName(messageContext.getSubjectTemplateName(),
				messageContext.getSubjectTemplateValues());
	}

	/**
	 * 
	 * @param messageContext
	 * @return {@link String}
	 */

	private String getMailBody(EmailMessageContext messageContext) {

		return templateService.loadByName(messageContext.getBodyTemplateName(), messageContext.getBodyTemplateValues());
	}
}