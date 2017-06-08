package org.egov.lams.notification.adapter;

import org.egov.lams.notification.broker.AgreementNotificationProducer;
import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.enums.Priority;
import org.egov.lams.notification.models.Agreement;
import org.egov.lams.notification.models.Sms;
import org.egov.lams.notification.service.SmsNotificationService;
import org.egov.lams.notification.types.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AgreementNotificationAdapter {

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementNotificationAdapter.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private SmsNotificationService smsNotificationService;

	@Autowired
	private AgreementNotificationProducer agreementNotificationProducer;

	@Autowired
	SmsRequest smsRequest;

	public void sendSmsNotification(Agreement agreement) {

		if(agreement.getWorkflowDetails().getAction() == null)
			sendCreateNotification(agreement);
		else if(agreement.getWorkflowDetails().getAction().equals("Approve"))
			sendApprovalNotification(agreement);
		else if(agreement.getWorkflowDetails().getAction().equals("Reject"))
			sendRejectedNotification(agreement);
	}
	
	public void sendCreateNotification(Agreement agreement) {

		SmsRequest smsRequest = new SmsRequest();

		smsRequest.setMessage(smsNotificationService.getSmsMessage(agreement));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		LOGGER.info("agreementSMS------------" + smsRequest);
		String smsRequestJson = getJson(smsRequest);

		
		 try { agreementNotificationProducer.sendMessage(propertiesManager.getSmsNotificationTopic(), "sms-notification", smsRequestJson); }
		 catch (Exception ex) { 
			 ex.printStackTrace();
			 }		 
	}

	public void sendApprovalNotification(Agreement agreement) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getApprovalMessage(agreement));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		LOGGER.info("ApprovalSMS------------" + smsRequest);
		String smsRequestJson = getJson(smsRequest);

		
		  try { agreementNotificationProducer.sendMessage(propertiesManager.getSmsNotificationTopic(), "sms-notification", smsRequestJson); }
		  catch (Exception ex) { 
			  ex.printStackTrace();
			  }	 
	}

	public void sendRejectedNotification(Agreement agreement) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getRejectedMessage(agreement));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		LOGGER.info("RejectedSMS------------" + smsRequest);
		String smsRequestJson = getJson(smsRequest);
		
		  try { agreementNotificationProducer.sendMessage(propertiesManager.getSmsNotificationTopic(), "sms-notification", smsRequestJson); }
		  catch (Exception ex) { 
			  ex.printStackTrace(); 
			  }
	}

	private String getJson(SmsRequest smsRequest) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(smsRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
