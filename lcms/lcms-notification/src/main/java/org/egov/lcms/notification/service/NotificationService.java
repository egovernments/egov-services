package org.egov.lcms.notification.service;

import java.util.HashMap;
import java.util.Map;
import org.egov.lcms.notification.model.AdvocatePayment;
import org.egov.lcms.notification.model.AdvocatePaymentRequest;
import org.egov.lcms.notification.model.Case;
import org.egov.lcms.notification.model.CaseRequest;
import org.egov.lcms.notification.model.HearingDetails;
import org.egov.lcms.notification.model.Opinion;
import org.egov.lcms.notification.model.OpinionRequest;
import org.egov.lcms.notification.model.ParaWiseComment;
import org.egov.lcms.notification.model.Summon;
import org.egov.lcms.notification.model.SummonRequest;
import org.egov.lcms.notification.util.TimeStampUtil;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	public void sendEmailAndSmsForSummon(SummonRequest summonRequest) {

		Map<Object, Object> summonMessage = new HashMap<Object, Object>();

		for (Summon summon : summonRequest.getSummons()) {

			if (summon != null) {

				summonMessage.put("Reference No", summon.getSummonReferenceNo());
				summonMessage.put("Dated", TimeStampUtil.getDateWithoutTimezone(summon.getSummonDate()));
				summonMessage.put("ULB Name", summon.getTenantId());

				summonMessage.put("Assigning Authority", "");
				// Need mobile and Email details
			}
		}
	}

	public void sendEmailAndSmsForAssignAdvocate(SummonRequest summonRequest) {

		Map<Object, Object> assignAdvocateMessage = new HashMap<Object, Object>();

		for (Summon summon : summonRequest.getSummons()) {

			assignAdvocateMessage.put("Reference No", summon.getSummonReferenceNo());
			assignAdvocateMessage.put("Dated", TimeStampUtil.getDateWithoutTimezone(summon.getSummonDate()));
			assignAdvocateMessage.put("ULB Name", summon.getTenantId());

			assignAdvocateMessage.put("Case Registration Authority", "");
			assignAdvocateMessage.put("Legal Clerk", "");

			// Need mobile and Email details
		}
	}

	public void sendEmailAndSmsForCaseRegistration(CaseRequest caseRequest) {

		Map<Object, Object> caseRegistraionMessage = new HashMap<Object, Object>();

		for (Case caseObj : caseRequest.getCases()) {

			caseRegistraionMessage.put("caseRegNo", caseObj.getCaseRefernceNo());
			caseRegistraionMessage.put("ULB Name", caseObj.getTenantId());

			caseRegistraionMessage.put("Legal Clerk", "");

			// Need mobile and Email details
		}
	}

	public void sendEmailAndSmsForVakalatnama(CaseRequest caseRequest) {

		Map<Object, Object> vakalatnamaMessage = new HashMap<Object, Object>();

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getIsVakalatnamaGenerated()) {

				vakalatnamaMessage.put("Dated", caseObj.getVakalatnamaGenerationDate());
				vakalatnamaMessage.put("Case details date", caseObj.getCaseRegistrationDate());
				vakalatnamaMessage.put("ULB Name", caseObj.getTenantId());

				vakalatnamaMessage.put("Concerned Department", "");
				vakalatnamaMessage.put("Advocate", "");

				// Need mobile and Email details
			}
		}
	}

	public void sendEmailAndSmsForParawiseComments(CaseRequest caseRequest) {

		Map<Object, Object> parawiseMessage = new HashMap<Object, Object>();

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getParawiseComments() != null && caseObj.getParawiseComments().size() > 0) {

				for (ParaWiseComment paraWiseComment : caseObj.getParawiseComments()) {

					parawiseMessage.put("Reference No", caseObj.getCaseRefernceNo());
					parawiseMessage.put("Dated",
							TimeStampUtil.getDateWithoutTimezone(paraWiseComment.getParawiseCommentsReceivedDate()));
					parawiseMessage.put("ULB Name", caseObj.getTenantId());

					parawiseMessage.put("Legal Clerk/Law Officer", "");

					// Need mobile and Email details
				}
			}
		}
	}

	public void sendEmailAndSmsForHearingProcessDetails(CaseRequest caseRequest) {

		Map<Object, Object> hearingProcessMessage = new HashMap<Object, Object>();

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getHearingDetails() != null || caseObj.getHearingDetails().size() > 0) {

				hearingProcessMessage.put("Reference No", caseObj.getCaseRefernceNo());
				hearingProcessMessage.put("Dated",
						TimeStampUtil.getDateWithoutTimezone(caseObj.getCaseRegistrationDate()));
				hearingProcessMessage.put("ULB Name", caseObj.getTenantId());
				hearingProcessMessage.put("Court", caseObj.getSummon().getCourtName());

				for (HearingDetails hearingDetails : caseObj.getHearingDetails()) {
					
					hearingProcessMessage.put("Next Hearing Date",
							TimeStampUtil.getDateWithoutTimezone(hearingDetails.getNextHearingDate()));
					hearingProcessMessage.put("Next Hearing Time",hearingDetails.getNextHearingTime());
				}
				
				hearingProcessMessage.put("Legal Department/Concerned Department", "");
				
				// Need mobile and Email details

			}
		}
	}

	public void sendEmailAndSmsForAdvocatePayment(AdvocatePaymentRequest advocatePaymentRequest) {

		Map<Object, Object> advocatePaymentMessage = new HashMap<Object, Object>();

		for (AdvocatePayment advocatePayment : advocatePaymentRequest.getAdvocatePayments()) {
			
			advocatePaymentMessage.put("Advocate Name", advocatePayment.getAdvocate().getName());
			advocatePaymentMessage.put("Case No", advocatePayment.getCaseNo());	
			advocatePaymentMessage.put("Case Date", "");//case registraion form
			advocatePaymentMessage.put("Demand No", "");//Advocate form
			advocatePaymentMessage.put("Letter No", "");//Approval letter Form					
			advocatePaymentMessage.put("ULB Name", advocatePayment.getTenantId());

			
			advocatePaymentMessage.put("Legal Advisor", "");
			advocatePaymentMessage.put("Legal Clerk", "");
			advocatePaymentMessage.put("Approval Legal Clerk", "");
			advocatePaymentMessage.put("Sanctioning Legal Advisor", "");
		}

	}

	public void sendEmailAndSmsForOpinion(OpinionRequest opinionRequest) {

		Map<Object, Object> opinionMessage = new HashMap<Object, Object>();

		for (Opinion opinion : opinionRequest.getOpinions()) {

			opinionMessage.put("Opinion No", opinion.getOpinionOn());
			opinionMessage.put("Department Name", opinion.getDepartmentName());
			opinionMessage.put("Dated", opinion.getOpinionRequestDate());

			opinionMessage.put("Advocate", "");
			opinionMessage.put("Approving Authority", "");
			opinionMessage.put("Legal clerk", "");

			// Need mobile and Email details
		}
	}
}

/**
 * Map<Object, Object> propertyMessage = new HashMap<Object, Object>(); for
 * (Property property : properties) {
 * 
 * propertyMessage.put("acknowledgementNo",
 * property.getPropertyDetail().getApplicationNo());
 * propertyMessage.put("upicNo", property.getUpicNumber());
 * propertyMessage.put("assessmentDate", property.getAssessmentDate());
 * propertyMessage.put("tenantId", property.getTenantId());
 * 
 * for (User user : property.getOwners()) {
 * 
 * propertyMessage.put("name", user.getName()); propertyMessage.put("tenantId",
 * user.getTenantId()); String emailAddress = user.getEmailId(); String
 * mobileNumber = user.getMobileNumber(); String message =
 * notificationUtil.buildSmsMessage(propertiesManager.getAlterationAcknowledgementSms(),
 * propertyMessage); SmsMessage smsMessage = new SmsMessage(message,
 * mobileNumber); EmailMessageContext emailMessageContext = new
 * EmailMessageContext();
 * emailMessageContext.setBodyTemplateName(propertiesManager.getAlterationAcknowledgementEmailBody());
 * emailMessageContext.setBodyTemplateValues(propertyMessage);
 * emailMessageContext
 * .setSubjectTemplateName(propertiesManager.getAlterationAcknowledgementEmailSubject());
 * emailMessageContext.setSubjectTemplateValues(propertyMessage); EmailRequest
 * emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
 * EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest,
 * emailAddress); kafkaTemplate.send(propertiesManager.getSmsNotification(),
 * smsMessage); kafkaTemplate.send(propertiesManager.getEmailNotification(),
 * emailMessage); } }
 * 
 * @param summonRequest
 *            1.Assigning Authority 2.Reference No 3.Dated 4.ULB Name
 *            TimeStampUtil.getDateWithoutTimezone(property.getAssessmentDate());
 */
