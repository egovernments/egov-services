package org.egov.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandResponse;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TitleTransfer;
import org.egov.models.User;
import org.egov.models.VacancyRemissionRequest;
import org.egov.notification.config.PropertiesManager;
import org.egov.notification.model.EmailMessage;
import org.egov.notification.model.EmailMessageContext;
import org.egov.notification.model.EmailRequest;
import org.egov.notification.model.SmsMessage;
import org.egov.notification.repository.DemandRepository;
import org.egov.notification.repository.NoticeRepository;
import org.egov.notification.repository.PropertyRepository;
import org.egov.notificationConsumer.NotificationUtil;
import org.egov.notificationConsumer.TimeStampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Yosadhara
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
	DemandRepository demandRepository;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	NoticeRepository noticeRepository;

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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getDemandAcknowledgementSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getDemandAcknowledgementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getDemandAcknowledgementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getDemandApproveSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getDemandApproveEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getDemandApproveEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getDemandTransferfeeSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getDemandTransferfeeEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getDemandTransferfeeEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getDemandRejectSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getDemandRejectEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getDemandRejectEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
			String assessmentDate = TimeStampUtil.getDateWithoutTimezone(property.getAssessmentDate());
			propertyMessage.put("assessmentDate", assessmentDate);
			propertyMessage.put("tenantId", property.getTenantId());

			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(propertiesManager.getPropertyAcknowledgementSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getPropertyAcknowledgementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getPropertyAcknowledgementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Property approval
	 * 
	 * @param properties
	 */
	public void propertyApprove(List<Property> properties, RequestInfo requestInfo) {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : properties) {

			propertyMessage.put("acknowledgementNo", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("upicNo", property.getUpicNumber());
			String assessmentDate = TimeStampUtil.getDateWithoutTimezone(property.getAssessmentDate());
			propertyMessage.put("assessmentDate", assessmentDate);
			propertyMessage.put("tenantId", property.getTenantId());
			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(requestInfo);
			Double propertyTax = getTotalTax(property.getTenantId(), property, requestInfoWrapper);

			propertyMessage.put("propertyTax", propertyTax);
			String occupancytDate = TimeStampUtil.getDateWithoutTimezone(property.getOccupancyDate());
			propertyMessage.put("effectiveDate", occupancytDate);
			propertyMessage.put("municipalityName", property.getTenantId());
			for (User user : property.getOwners()) {

				propertyMessage.put("name", user.getName());
				propertyMessage.put("tenantId", user.getTenantId());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(propertiesManager.getPropertyApproveSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getPropertyApproveEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getPropertyApproveEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	/**
	 * This method is to send email and sms for Property rejection
	 * 
	 * @param properties
	 * @throws Exception
	 */

	public void propertyReject(PropertyRequest propertyRequest) throws Exception {

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		for (Property property : propertyRequest.getProperties()) {
			EmailMessage emailMessage = null;
			RequestInfo requestInfo= propertyRequest.getRequestInfo();
			String action = property.getPropertyDetail().getWorkFlowDetails().getAction();
			String tenantId = property.getTenantId();
			String applicationNo = property.getPropertyDetail().getApplicationNo();
			String noticeType = propertiesManager.getRejectionLetter();
			String comments = property.getPropertyDetail().getWorkFlowDetails().getComments();
			String localHostAddress = propertiesManager.getEgovServicesHost();
			String rejectionDownloadPath = propertiesManager.getRejectionDownloadPath();
			String filestorePath = localHostAddress + rejectionDownloadPath;

			propertyMessage.put("Acknowledgement Number", applicationNo);
			propertyMessage.put("Corporation/Municipality/Nagara Panchayat name", tenantId);
			
			if (comments != null) {
				propertyMessage.put("Approval/Rejection comment", comments);
			} else {
				propertyMessage.put("Approval/Rejection comment", propertiesManager.getApprovalOrRejectionComment());
			}
			
			if (action.equalsIgnoreCase(propertiesManager.getCancel())) {
				
				if (tenantId != null && applicationNo != null) {
					
					filestorePath = filestorePath.replace(":tenantId", tenantId);
					String fileStoreId = noticeRepository.getfileStoreId(requestInfo, tenantId, applicationNo, noticeType);
					log.info("filestoreId for download link: " + fileStoreId);
					if (fileStoreId != null) {

						filestorePath = filestorePath.replace(":fileStoreId", fileStoreId);
					}

					String urlLink = "<a href =" + filestorePath + ">Download Link</a>";
					propertyMessage.put("rejectionLetterUrl", urlLink);
				}
			}
			

			for (User user : property.getOwners()) {

				propertyMessage.put("Owner name", user.getName());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(propertiesManager.getPropertyRejectSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setSubjectTemplateName(propertiesManager.getPropertyRejectEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				
				if (action.equalsIgnoreCase(propertiesManager.getCancel())) {
					emailMessageContext.setBodyTemplateName(propertiesManager.getPropertyRejectCancelEmailBody());
					emailMessageContext.setBodyTemplateValues(propertyMessage);
					emailMessage = notificationUtil.buildRejectionEmailTemplate(emailMessageContext,
							emailAddress);
					
				} else {
					emailMessageContext.setBodyTemplateName(propertiesManager.getPropertyRejectEmailBody());
					emailMessageContext.setBodyTemplateValues(propertyMessage);
					
					EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
					emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				}				
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil
						.buildSmsMessage(propertiesManager.getRevisionPetitionAcknowledgementSms(), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setBodyTemplateName(propertiesManager.getRevisionPetitionAcknowledgementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(propertiesManager.getRevisionPetitionAcknowledgementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getRevisionPetitionHearingSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getRevisionPetitionHearingEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getRevisionPetitionHearingEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getRevisionPetitionEndorsementSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getRevisionPetitionEndorsementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(propertiesManager.getRevisionPetitionEndorsementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	/**
	 * This method is for sending email and sms for property alteration
	 * acknowledgement
	 * 
	 * @param properties
	 */
	public void alterationAcknowledgement(List<Property> properties) {

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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getAlterationAcknowledgementSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getAlterationAcknowledgementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext
						.setSubjectTemplateName(propertiesManager.getAlterationAcknowledgementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	/**
	 * This method is for sending email and sms for approve property alteration
	 * 
	 * @param properties
	 */
	public void approvePropertyAlteration(List<Property> properties) {

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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getAlterationApproveSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getAlterationApproveEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getAlterationApproveEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	/**
	 * This method is for sending email and sms for reject property alteration
	 * 
	 * @param properties
	 */
	public void rejectPropertyAlteration(List<Property> properties) {

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
				String message = notificationUtil.buildSmsMessage(propertiesManager.getAlterationRejectSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setBodyTemplateName(propertiesManager.getAlterationRejectEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);
				emailMessageContext.setSubjectTemplateName(propertiesManager.getAlterationRejectEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);
				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	private Double getTotalTax(String tenantId, Property property, RequestInfoWrapper requestInfo) {
		Double totalPropertyTax = 0.0;
		try {
			

			Set<String> id = new HashSet<String>();
			List<Demand> demands=property.getDemands();
			demands.forEach((demand) -> id.add(demand.getId()));
			DemandResponse demandResponse = demandRepository.getDemands(id, tenantId, requestInfo);
			if (demandResponse != null) {
				for (Demand demand : demandResponse.getDemands()) {

					for (DemandDetail demandDetail : demand.getDemandDetails()) {

						totalPropertyTax += demandDetail.getTaxAmount().doubleValue();

					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return totalPropertyTax;
	}

	public void acknowledgeVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) {

		RequestInfo requestInfo = vacancyRemissionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String tenantId = vacancyRemissionRequest.getVacancyRemission().getStateId();
		String upicNo = vacancyRemissionRequest.getVacancyRemission().getUpicNo();
		List<Property> properties = new ArrayList<>();
		try {
			properties = propertyRepository.getProperty(tenantId, upicNo, requestInfoWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Property property : properties) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			propertyMessage.put("Property Number", property.getUpicNumber());
			propertyMessage.put("Application number", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("Municipality/Corporation name", property.getTenantId());
			propertyMessage.put("Application generated Date",
					property.getPropertyDetail().getAuditDetails().getLastModifiedTime());

			for (User user : property.getOwners()) {

				propertyMessage.put("Property Owner name", user.getName());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil
						.buildSmsMessage(propertiesManager.getVacancyRemissionsAcknowledgementSms(), propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext
						.setSubjectTemplateName(propertiesManager.getVacancyRemissionsAcknowledgementEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				emailMessageContext
						.setBodyTemplateName(propertiesManager.getVacancyRemissionsAcknowledgementEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);

				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	public void approveVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) {

		RequestInfo requestInfo = vacancyRemissionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String tenantId = vacancyRemissionRequest.getVacancyRemission().getStateId();
		String upicNo = vacancyRemissionRequest.getVacancyRemission().getUpicNo();
		List<Property> properties = new ArrayList<>();
		try {
			properties = propertyRepository.getProperty(tenantId, upicNo, requestInfoWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Property property : properties) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			propertyMessage.put("Property Number", property.getUpicNumber());
			propertyMessage.put("Application number", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("Municipality/Corporation name", property.getTenantId());
			propertyMessage.put("Application generated Date",
					property.getPropertyDetail().getAuditDetails().getLastModifiedTime());

			for (User user : property.getOwners()) {

				propertyMessage.put("Property Owner name", user.getName());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(propertiesManager.getVacancyRemissionsApproveSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setSubjectTemplateName(propertiesManager.getVacancyRemissionsApproveEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				emailMessageContext.setBodyTemplateName(propertiesManager.getVacancyRemissionsApproveEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);

				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	public void rejectVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) {

		RequestInfo requestInfo = vacancyRemissionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String tenantId = vacancyRemissionRequest.getVacancyRemission().getStateId();
		String upicNo = vacancyRemissionRequest.getVacancyRemission().getUpicNo();
		List<Property> properties = new ArrayList<>();
		try {
			properties = propertyRepository.getProperty(tenantId, upicNo, requestInfoWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Property property : properties) {

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			propertyMessage.put("Property Number", property.getUpicNumber());
			propertyMessage.put("Application number", property.getPropertyDetail().getApplicationNo());
			propertyMessage.put("Municipality/Corporation name", property.getTenantId());
			propertyMessage.put("Application generated Date",
					property.getPropertyDetail().getAuditDetails().getLastModifiedTime());

			for (User user : property.getOwners()) {

				propertyMessage.put("Property Owner name", user.getName());
				String emailAddress = user.getEmailId();
				String mobileNumber = user.getMobileNumber();
				String message = notificationUtil.buildSmsMessage(propertiesManager.getVacancyRemissionsRejectSms(),
						propertyMessage);
				SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

				EmailMessageContext emailMessageContext = new EmailMessageContext();
				emailMessageContext.setSubjectTemplateName(propertiesManager.getVacancyRemissionsRejectEmailSubject());
				emailMessageContext.setSubjectTemplateValues(propertyMessage);
				emailMessageContext.setBodyTemplateName(propertiesManager.getVacancyRemissionsRejectEmailBody());
				emailMessageContext.setBodyTemplateValues(propertyMessage);

				EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
				EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

				kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
			}
		}
	}

	public void acknowledgeTitleTransfer(TitleTransfer titleTransfer) {

		Map<Object, Object> titleTransferMessage = new HashMap<Object, Object>();
		titleTransferMessage.put("Property Number", titleTransfer.getUpicNo());
		titleTransferMessage.put("Corporation/Municipality/Nagara Panchayat name", titleTransfer.getTenantId());

		for (User user : titleTransfer.getNewOwners()) {

			titleTransferMessage.put("Owner name", user.getName());
			String emailAddress = user.getEmailId();
			String mobileNumber = user.getMobileNumber();
			String message = notificationUtil.buildSmsMessage(propertiesManager.getTitleTransferAcknowledgementSms(),
					titleTransferMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setSubjectTemplateName(propertiesManager.getTitleTransferAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(titleTransferMessage);
			emailMessageContext.setBodyTemplateName(propertiesManager.getTitleTransferAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(titleTransferMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

			kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
			kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
		}
	}

	public void approveTitleTransfer(TitleTransfer titleTransfer) {

		Map<Object, Object> titleTransferMessage = new HashMap<Object, Object>();
		titleTransferMessage.put("Property Number", titleTransfer.getUpicNo());
		titleTransferMessage.put("Corporation/Municipality/Nagara Panchayat name", titleTransfer.getTenantId());

		for (User user : titleTransfer.getNewOwners()) {

			titleTransferMessage.put("Owner name", user.getName());
			String emailAddress = user.getEmailId();
			String mobileNumber = user.getMobileNumber();
			String message = notificationUtil.buildSmsMessage(propertiesManager.getTitleTransferApproveSms(),
					titleTransferMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setSubjectTemplateName(propertiesManager.getTitleTransferApproveEmailSubject());
			emailMessageContext.setSubjectTemplateValues(titleTransferMessage);
			emailMessageContext.setBodyTemplateName(propertiesManager.getTitleTransferApproveEmailBody());
			emailMessageContext.setBodyTemplateValues(titleTransferMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

			kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
			kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
		}
	}

	public void rejectTitleTransfer(TitleTransfer titleTransfer) {

		Map<Object, Object> titleTransferMessage = new HashMap<Object, Object>();
		titleTransferMessage.put("Property Number", titleTransfer.getUpicNo());
		titleTransferMessage.put("Corporation/Municipality/Nagara Panchayat name", titleTransfer.getTenantId());
		titleTransferMessage.put("Rejection Comment", propertiesManager.getTitleTransferRejectComment());

		for (User user : titleTransfer.getNewOwners()) {

			titleTransferMessage.put("Owner name", user.getName());
			String emailAddress = user.getEmailId();
			String mobileNumber = user.getMobileNumber();
			String message = notificationUtil.buildSmsMessage(propertiesManager.getTitleTransferRejectSms(),
					titleTransferMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);

			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setSubjectTemplateName(propertiesManager.getTitleTransferRejectEmailSubject());
			emailMessageContext.setSubjectTemplateValues(titleTransferMessage);
			emailMessageContext.setBodyTemplateName(propertiesManager.getTitleTransferRejectEmailBody());
			emailMessageContext.setBodyTemplateValues(titleTransferMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

			kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
			kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
		}
	}
}
