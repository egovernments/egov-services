package org.egov.lams.notification.adapter;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.egov.lams.notification.broker.AgreementNotificationProducer;
import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.Agreement;
import org.egov.lams.notification.model.Allottee;
import org.egov.lams.notification.model.Asset;
import org.egov.lams.notification.repository.AllotteeRepository;
import org.egov.lams.notification.repository.AssetRepository;
import org.egov.lams.notification.service.SmsNotificationService;
import org.egov.lams.notification.web.contract.AgreementRequest;
import org.egov.lams.notification.web.contract.RequestInfo;
import org.egov.lams.notification.web.contract.SmsRequest;

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
	private AssetRepository assetRepository;

	@Autowired
	private AllotteeRepository allotteeRepository;
	
	@Autowired
	private SmsNotificationService smsNotificationService;

	@Autowired
	private AgreementNotificationProducer agreementNotificationProducer;

	@Autowired
	SmsRequest smsRequest;

	public void sendSmsNotification(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(),agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(),agreement.getTenantId(),requestInfo);
		
		if(!isEmpty(agreement.getWorkflowDetails()))
		{
		if(agreement.getWorkflowDetails().getAction() == null)
			sendCreateNotification(agreement, asset, allottee);
		else if(agreement.getWorkflowDetails().getAction().equals("Approve"))
			sendApprovalNotification(agreement, asset, allottee);
		else if(agreement.getWorkflowDetails().getAction().equals("Reject"))
			sendRejectedNotification(agreement, asset, allottee);
		}
	}
	
	public void sendCreateNotification(Agreement agreement, Asset asset, Allottee allottee) {

		SmsRequest smsRequest = new SmsRequest();

		smsRequest.setMessage(smsNotificationService.getSmsMessage(agreement, asset, allottee));
		smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

		LOGGER.info("agreementSMS------------" + smsRequest);
		String smsRequestJson = getJson(smsRequest);

		
		 try { agreementNotificationProducer.sendMessage(propertiesManager.getSmsNotificationTopic(), "sms-notification", smsRequestJson); }
		 catch (Exception ex) { 
			 ex.printStackTrace();
			 }		 
	}

	public void sendApprovalNotification(Agreement agreement, Asset asset, Allottee allottee) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getApprovalMessage(agreement, asset, allottee));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		LOGGER.info("ApprovalSMS------------" + smsRequest);
		String smsRequestJson = getJson(smsRequest);

		
		  try { agreementNotificationProducer.sendMessage(propertiesManager.getSmsNotificationTopic(), "sms-notification", smsRequestJson); }
		  catch (Exception ex) { 
			  ex.printStackTrace();
			  }	 
	}

	public void sendRejectedNotification(Agreement agreement, Asset asset, Allottee allottee) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getRejectedMessage(agreement, asset, allottee));
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
