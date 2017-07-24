package org.egov.lams.notification.adapter;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.Agreement;
import org.egov.lams.notification.model.Allottee;
import org.egov.lams.notification.model.Asset;
import org.egov.lams.notification.model.enums.Action;
import org.egov.lams.notification.repository.AllotteeRepository;
import org.egov.lams.notification.repository.AssetRepository;
import org.egov.lams.notification.repository.TenantRepository;
import org.egov.lams.notification.service.NotificationService;
import org.egov.lams.notification.web.contract.AgreementRequest;
import org.egov.lams.notification.web.contract.EmailRequest;
import org.egov.lams.notification.web.contract.RequestInfo;
import org.egov.lams.notification.web.contract.SmsRequest;
import org.egov.lams.notification.web.contract.Tenant;
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
	private NotificationService notificationService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public void sendNotification(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();

		RequestInfo requestInfo = agreementRequest.getRequestInfo();

		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(), agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(), agreement.getTenantId(),
				requestInfo);
		Tenant tenant = tenantRepository.fetchTenantByCode(agreement.getTenantId());

		if (!isEmpty(agreement.getWorkflowDetails())) {
			if (agreement.getWorkflowDetails().getAction() == null)
				sendCreateNotification(agreement, asset, allottee, tenant);
			else if (agreement.getWorkflowDetails().getAction().equals("Approve"))
				sendApprovalNotification(agreement, asset, allottee, tenant);
			else if (agreement.getWorkflowDetails().getAction().equals("Reject"))
				sendRejectedNotification(agreement, asset, allottee, tenant);
		}
	}

	public void sendCreateNotification(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {

		SmsRequest smsRequest = new SmsRequest();
		EmailRequest emailRequest = new EmailRequest();

		if (agreement.getAction().equals(Action.CREATE)) {

			smsRequest.setMessage(notificationService.getCreateInitiateMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

			emailRequest.setBody(notificationService.getCreateInitiateMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getCreateSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("agreementcreateInitSMS------------" + smsRequest);
		} else if (agreement.getAction().equals(Action.EVICTION)) {

			smsRequest.setMessage(notificationService.getEvictInitiateMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

			emailRequest.setBody(notificationService.getEvictInitiateMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getEvictSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("agreementevictInitSMS------------" + smsRequest);
		}

		try {
			kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), smsRequest);
			kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), emailRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendApprovalNotification(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		SmsRequest smsRequest = new SmsRequest();
		EmailRequest emailRequest = new EmailRequest();

		if (agreement.getAction().equals(Action.CREATE)) {
			smsRequest.setMessage(notificationService.getCreateApprovalMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

			emailRequest.setBody(notificationService.getCreateApprovalMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getCreateSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("CreateApprovalSMS------------" + smsRequest);
		} else if (agreement.getAction().equals(Action.EVICTION)) {

			smsRequest.setMessage(notificationService.getEvictApprovalMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

			emailRequest.setBody(notificationService.getEvictApprovalMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getEvictSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("evictApprovalSMS------------" + smsRequest);
		}
		try {
			kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), smsRequest);
			kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), emailRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendRejectedNotification(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {

		SmsRequest smsRequest = new SmsRequest();
		EmailRequest emailRequest = new EmailRequest();

		if (agreement.getAction().equals(Action.CREATE)) {
			smsRequest.setMessage(notificationService.getCreateRejectedMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(agreement.getAllottee().getMobileNumber().toString());

			emailRequest.setBody(notificationService.getCreateRejectedMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getCreateSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("createRejectedSMS------------" + smsRequest);

		} else if (agreement.getAction().equals(Action.EVICTION)) {

			smsRequest.setMessage(notificationService.getEvictRejectedMessage(agreement, asset, allottee, tenant));
			smsRequest.setMobileNumber(allottee.getMobileNumber().toString());

			emailRequest.setBody(notificationService.getEvictRejectedMessage(agreement, asset, allottee, tenant));
			emailRequest.setSubject(notificationService.getEvictSubject(agreement));
			emailRequest.setEmail(allottee.getEmailId());
			log.info("evictRejectedSMS------------" + smsRequest);
		}
		try {
			kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), smsRequest);
			kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), smsRequest);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
