package org.egov.lams.notification.adapter;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.Agreement;
import org.egov.lams.notification.model.Allottee;
import org.egov.lams.notification.model.Asset;
import org.egov.lams.notification.model.City;
import org.egov.lams.notification.repository.AllotteeRepository;
import org.egov.lams.notification.repository.AssetRepository;
import org.egov.lams.notification.repository.TenantRepository;
import org.egov.lams.notification.service.SmsNotificationService;
import org.egov.lams.notification.web.contract.AgreementRequest;
import org.egov.lams.notification.web.contract.RequestInfo;
import org.egov.lams.notification.web.contract.SmsRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementNotificationAdapter {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AllotteeRepository allotteeRepository;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private SmsNotificationService smsNotificationService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public void sendSmsNotification(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(),agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(),agreement.getTenantId(),requestInfo);
		City city = tenantRepository.fetchTenantByCode(agreement.getTenantId());

		if(!isEmpty(agreement.getWorkflowDetails()))
		{
		if(agreement.getWorkflowDetails().getAction() == null)
			sendCreateNotification(agreement, asset, allottee, city);
		else if(agreement.getWorkflowDetails().getAction().equals("Approve"))
			sendApprovalNotification(agreement, asset, allottee, city);
		else if(agreement.getWorkflowDetails().getAction().equals("Reject"))
			sendRejectedNotification(agreement, asset, allottee, city);
		}
	}
	
	public void sendCreateNotification(Agreement agreement, Asset asset, Allottee allottee, City city) {

		SmsRequest smsRequest = new SmsRequest();

		smsRequest.setMessage(smsNotificationService.getSmsMessage(agreement, asset, allottee, city));
		smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

		log.info("agreementSMS------------" + smsRequest);		
		 try { 
			 kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(), propertiesManager.getSmsNotificationTopicKey(), smsRequest); }
		 catch (Exception ex) { 
			 ex.printStackTrace();
			 }		 
	}

	public void sendApprovalNotification(Agreement agreement, Asset asset, Allottee allottee, City city) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getApprovalMessage(agreement, asset, allottee, city));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		log.info("ApprovalSMS------------" + smsRequest);
		
		  try { kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(), propertiesManager.getSmsNotificationTopicKey(), smsRequest); }
		  catch (Exception ex) { 
			  ex.printStackTrace();
			  }	 
	}

	public void sendRejectedNotification(Agreement agreement, Asset asset, Allottee allottee, City city) {

		SmsRequest smsRequest = new SmsRequest();
		smsRequest.setMessage(smsNotificationService.getRejectedMessage(agreement, asset, allottee, city));
		smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

		log.info("RejectedSMS------------" + smsRequest);
		
		  try { kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(), propertiesManager.getSmsNotificationTopicKey(), smsRequest); }
		  catch (Exception ex) { 
			  ex.printStackTrace(); 
			  }
	}
}
