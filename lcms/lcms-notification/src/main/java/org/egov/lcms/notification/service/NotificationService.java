package org.egov.lcms.notification.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.lcms.notification.model.AdvocateCharge;
import org.egov.lcms.notification.model.AdvocatePayment;
import org.egov.lcms.notification.model.AdvocatePaymentRequest;
import org.egov.lcms.notification.model.Case;
import org.egov.lcms.notification.model.CaseRequest;
import org.egov.lcms.notification.model.EmailMessage;
import org.egov.lcms.notification.model.EmailMessageContext;
import org.egov.lcms.notification.model.EmailRequest;
import org.egov.lcms.notification.model.HearingDetails;
import org.egov.lcms.notification.model.Opinion;
import org.egov.lcms.notification.model.OpinionRequest;
import org.egov.lcms.notification.model.ParaWiseComment;
import org.egov.lcms.notification.model.SmsMessage;
import org.egov.lcms.notification.model.Summon;
import org.egov.lcms.notification.model.SummonRequest;
import org.egov.lcms.notification.model.UserDetail;
import org.egov.lcms.notification.repository.AdvocateRepository;
import org.egov.lcms.notification.repository.CaseRepository;
import org.egov.lcms.notification.repository.MdmsRepository;
import org.egov.lcms.notification.repository.UserReository;
import org.egov.lcms.notification.util.NotificationUtil;
import org.egov.lcms.notification.util.TimeStampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	UserReository userReository;

	@Autowired
	CaseRepository caseRepository;

	@Autowired
	AdvocateRepository advocateRepository;

	@Autowired
	MdmsRepository mdmsRepository;

	@Autowired
	NotificationUtil notificationUtil;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	private Boolean caseRegistrationAuthority = Boolean.FALSE;

	private Boolean legalClerk = Boolean.FALSE;

	private Boolean concernedDepartment = Boolean.FALSE;

	private Boolean advocate = Boolean.FALSE;

	private Boolean legalAdvisor = Boolean.FALSE;

	private Boolean approvalLegalClerk = Boolean.FALSE;

	private Boolean sanctioningLegalAdvisor = Boolean.FALSE;

	private Boolean approvingAuthority = Boolean.FALSE;

	public void sendEmailAndSmsForSummon(SummonRequest summonRequest) throws Exception {

		Map<Object, Object> summonMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Summon summon : summonRequest.getSummons()) {

			if (summon != null) {

				summonMessage.put("Reference No", summon.getSummonReferenceNo());
				summonMessage.put("Dated", TimeStampUtil.getDateWithoutTimezone(summon.getSummonDate()));
				summonMessage.put("ULB Name", summon.getTenantId());
				userDetails = userReository.getUser(summon.getTenantId(), roleCodes, summonRequest.getRequestInfo());

				for (UserDetail userDetail : userDetails) {

					summonMessage.put("Assigning Authority", userDetail.getName());

					if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

						SmsMessage smsMessage = getSMS(propertiesManager.getSummonSms(), summonMessage,
								userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}

					if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

						EmailMessage emailMessage = getEmail(propertiesManager.getSummonEmailSubject(),
								propertiesManager.getSummonEmailBody(), summonMessage, userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForAssignAdvocate(CaseRequest caseRequest) throws Exception {

		caseRegistrationAuthority = Boolean.TRUE;
		legalClerk = Boolean.TRUE;

		Map<Object, Object> assignAdvocateMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getSummon() != null) {

				Summon summon = caseObj.getSummon();
				assignAdvocateMessage.put("Reference No", summon.getSummonReferenceNo());
				assignAdvocateMessage.put("Dated", TimeStampUtil.getDateWithoutTimezone(summon.getSummonDate()));
				assignAdvocateMessage.put("ULB Name", summon.getTenantId());
				userDetails = userReository.getUser(summon.getTenantId(), roleCodes, caseRequest.getRequestInfo());

				for (UserDetail userDetail : userDetails) {

					assignAdvocateMessage.put("Case Registration Authority", userDetail.getName());
					assignAdvocateMessage.put("Legal Clerk", userDetail.getName());

					if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

						if (caseRegistrationAuthority) {

							SmsMessage smsMessage = getSMS(propertiesManager.getAssignAdvocateRegAuthSms(),
									assignAdvocateMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}

						if (legalClerk) {

							SmsMessage smsMessage = getSMS(propertiesManager.getAssignAdvocateLegalClerkSms(),
									assignAdvocateMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}
					}

					if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

						if (caseRegistrationAuthority) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getAssignAdvocateRegAuthEmailSubject(),
									propertiesManager.getAssignAdvocateRegAuthEmailBody(), assignAdvocateMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}

						if (legalClerk) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getAssignAdvocateLegalClerkEmailSubject(),
									propertiesManager.getAssignAdvocateLegalClerkEmailBody(), assignAdvocateMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForCaseRegistration(CaseRequest caseRequest) throws Exception {

		Map<Object, Object> caseRegistraionMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Case caseObj : caseRequest.getCases()) {

			caseRegistraionMessage.put("caseRegNo", caseObj.getCaseRefernceNo());
			caseRegistraionMessage.put("ULB Name", caseObj.getTenantId());
			userDetails = userReository.getUser(caseObj.getTenantId(), roleCodes, caseRequest.getRequestInfo());

			for (UserDetail userDetail : userDetails) {

				caseRegistraionMessage.put("Legal Clerk", userDetail.getName());

				if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

					SmsMessage smsMessage = getSMS(propertiesManager.getCaseRegLegalClerkSms(), caseRegistraionMessage,
							userDetail.getMobileNumber());
					kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
				}

				if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

					EmailMessage emailMessage = getEmail(propertiesManager.getCaseRegLegalClerkEmailSubject(),
							propertiesManager.getCaseRegLegalClerkEmailBody(), caseRegistraionMessage,
							userDetail.getEmailId());
					kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
				}
			}
		}
	}

	public void sendEmailAndSmsForVakalatnama(CaseRequest caseRequest) throws Exception {

		concernedDepartment = Boolean.TRUE;
		advocate = Boolean.TRUE;

		Map<Object, Object> vakalatnamaMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getIsVakalatnamaGenerated()) {

				vakalatnamaMessage.put("Dated",
						TimeStampUtil.getDateWithoutTimezone(caseObj.getVakalatnamaGenerationDate()));
				vakalatnamaMessage.put("Case details date",
						TimeStampUtil.getDateWithoutTimezone(caseObj.getCaseRegistrationDate()));
				vakalatnamaMessage.put("ULB Name", caseObj.getTenantId());
				userDetails = userReository.getUser(caseObj.getTenantId(), roleCodes, caseRequest.getRequestInfo());

				for (UserDetail userDetail : userDetails) {

					vakalatnamaMessage.put("Concerned Department", userDetail.getName());
					vakalatnamaMessage.put("Advocate", userDetail.getName());

					if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

						if (concernedDepartment) {

							SmsMessage smsMessage = getSMS(propertiesManager.getVakalatnamaConcernedDeptSms(),
									vakalatnamaMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}

						if (advocate) {

							SmsMessage smsMessage = getSMS(propertiesManager.getVakalatnamaAdvocateSms(),
									vakalatnamaMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}
					}
					if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

						if (concernedDepartment) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getVakalatnamaConcernedDeptEmailSubject(),
									propertiesManager.getVakalatnamaConcernedDeptEmailBody(), vakalatnamaMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}

						if (advocate) {

							EmailMessage emailMessage = getEmail(propertiesManager.getVakalatnamaAdvocateEmailSubject(),
									propertiesManager.getVakalatnamaAdvocateEmailBody(), vakalatnamaMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForParawiseComments(CaseRequest caseRequest) throws Exception {

		Map<Object, Object> parawiseMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getParawiseComments() != null && caseObj.getParawiseComments().size() > 0) {

				for (ParaWiseComment paraWiseComment : caseObj.getParawiseComments()) {

					parawiseMessage.put("Reference No", caseObj.getCaseRefernceNo());
					parawiseMessage.put("Dated",
							TimeStampUtil.getDateWithoutTimezone(paraWiseComment.getParawiseCommentsReceivedDate()));
					parawiseMessage.put("ULB Name", caseObj.getTenantId());
					userDetails = userReository.getUser(caseObj.getTenantId(), roleCodes, caseRequest.getRequestInfo());

					for (UserDetail userDetail : userDetails) {

						parawiseMessage.put("Legal Clerk/Law Officer", userDetail.getName());

						if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

							SmsMessage smsMessage = getSMS(propertiesManager.getParawiseCommentLegalClerkSms(),
									parawiseMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}

						if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getParawiseCommentLegalClerkEmailSubject(),
									propertiesManager.getParawiseCommentLegalClerkEmailBody(), parawiseMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForHearingProcessDetails(CaseRequest caseRequest) throws Exception {

		Map<Object, Object> hearingProcessMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getHearingDetails() != null || caseObj.getHearingDetails().size() > 0) {

				hearingProcessMessage.put("Reference No", caseObj.getCaseRefernceNo());
				hearingProcessMessage.put("Dated",
						TimeStampUtil.getDateWithoutTimezone(caseObj.getCaseRegistrationDate()));
				hearingProcessMessage.put("ULB Name", caseObj.getTenantId());

				userDetails = userReository.getUser(caseObj.getTenantId(), roleCodes, caseRequest.getRequestInfo());

				for (HearingDetails hearingDetails : caseObj.getHearingDetails()) {

					if (hearingDetails.getNextHearingDate() == null && hearingDetails.getNextHearingTime() == null) {

						hearingProcessMessage.put("Hearing Decision", hearingDetails.getCaseFinalDecision());

						for (UserDetail userDetail : userDetails) {

							hearingProcessMessage.put("Legal Department/Concerned Department", userDetail.getName());

							if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

								SmsMessage smsMessage = getSMS(propertiesManager.getHearingProcessSms(),
										hearingProcessMessage, userDetail.getMobileNumber());
								kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
							}

							if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

								EmailMessage emailMessage = getEmail(propertiesManager.getHearingProcessEmailSubject(),
										propertiesManager.getHearingProcessEmailBody(), hearingProcessMessage,
										userDetail.getEmailId());
								kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
							}
						}
					} else {

						hearingProcessMessage.put("Hearing Decision", hearingDetails.getCaseJudgeMent());
						hearingProcessMessage.put("Next Hearing Date",
								TimeStampUtil.getDateWithoutTimezone(hearingDetails.getNextHearingDate()));
						hearingProcessMessage.put("Next Hearing Time", hearingDetails.getNextHearingTime());

						for (UserDetail userDetail : userDetails) {

							hearingProcessMessage.put("Legal Department/Concerned Department", userDetail.getName());

							if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

								SmsMessage smsMessage = getSMS(propertiesManager.getNextHearingProcessSMS(),
										hearingProcessMessage, userDetail.getMobileNumber());
								kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
							}

							if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

								EmailMessage emailMessage = getEmail(propertiesManager.getHearingProcessEmailSubject(),
										propertiesManager.getNextHearingProcessEmailBody(), hearingProcessMessage,
										userDetail.getEmailId());
								kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
							}
						}
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForAdvocatePayment(AdvocatePaymentRequest advocatePaymentRequest) throws Exception {

		legalClerk = Boolean.TRUE;
		sanctioningLegalAdvisor = Boolean.TRUE;

		Map<Object, Object> advocatePaymentMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		Date date = new Date();
		Long currentDate = date.getTime();

		for (AdvocatePayment advocatePayment : advocatePaymentRequest.getAdvocatePayments()) {

			Set<String> uniqueSummonRefNos = new HashSet<String>();

			for (AdvocateCharge advocateCharge : advocatePayment.getAdvocateCharges()) {
				uniqueSummonRefNos.add(advocateCharge.getCaseDetails().getSummonReferenceNo());
			}

			String advocateName = advocateRepository.getAdvocateName(advocatePayment.getTenantId(),
					advocatePayment.getAdvocate().getCode(), advocatePaymentRequest.getRequestInfo());
			String summonRefNumbers = caseRepository.getCaseNumbers(advocatePayment.getTenantId(), uniqueSummonRefNos,
					advocatePaymentRequest.getRequestInfo());
			advocatePaymentMessage.put("Advocate Name", advocateName);
			advocatePaymentMessage.put("Case No", summonRefNumbers);
			advocatePaymentMessage.put("Case Date", TimeStampUtil.getDateWithoutTimezone(currentDate));
			advocatePaymentMessage.put("ULB Name", advocatePayment.getTenantId());
			userDetails = userReository.getUser(advocatePayment.getTenantId(), roleCodes,
					advocatePaymentRequest.getRequestInfo());

			for (UserDetail userDetail : userDetails) {

				advocatePaymentMessage.put("Legal Clerk", userDetail.getName());
				advocatePaymentMessage.put("Sanctioning Legal Advisor", userDetail.getName());

				if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

					if (legalClerk) {

						SmsMessage smsMessage = getSMS(propertiesManager.getAdvovatePaymentLegalClerkSms(),
								advocatePaymentMessage, userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}

					if (sanctioningLegalAdvisor) {

						SmsMessage smsMessage = getSMS(propertiesManager.getAdvovatePaymentSanctioningAuthSms(),
								advocatePaymentMessage, userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}
				}
				if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

					if (legalClerk) {

						EmailMessage emailMessage = getEmail(
								propertiesManager.getAdvovatePaymentLegalClerkEmailSubject(),
								propertiesManager.getAdvovatePaymentLegalClerkEmailBody(), advocatePaymentMessage,
								userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}
					if (sanctioningLegalAdvisor) {

						EmailMessage emailMessage = getEmail(
								propertiesManager.getAdvovatePaymentSanctioningAuthEmailSubject(),
								propertiesManager.getAdvovatePaymentSanctioningAuthEmailBody(), advocatePaymentMessage,
								userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForUpdateAdvocatePayment(AdvocatePaymentRequest advocatePaymentRequest)
			throws Exception {

		Map<Object, Object> advocatePaymentMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (AdvocatePayment advocatePayment : advocatePaymentRequest.getAdvocatePayments()) {

			advocatePaymentMessage.put("Advocate Name", advocatePayment.getAdvocate().getName());
			advocatePaymentMessage.put("Demand No", advocatePayment.getCode());
			advocatePaymentMessage.put("ULB Name", advocatePayment.getTenantId());
			userDetails = userReository.getUser(advocatePayment.getTenantId(), roleCodes,
					advocatePaymentRequest.getRequestInfo());

			if (advocatePayment.getResolutionNo() != null && advocatePayment.getModeOfPayment() == null) {

				legalAdvisor = Boolean.TRUE;
				approvalLegalClerk = Boolean.TRUE;
				advocatePaymentMessage.put("Letter No", advocatePayment.getResolutionNo());

				for (UserDetail userDetail : userDetails) {

					advocatePaymentMessage.put("Legal Advisor", userDetail.getName());
					advocatePaymentMessage.put("Approval Legal Clerk", userDetail.getName());

					if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

						if (legalAdvisor) {

							SmsMessage smsMessage = getSMS(propertiesManager.getAdvovatePaymentLegalAdvisorSms(),
									advocatePaymentMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}

						if (approvalLegalClerk) {

							SmsMessage smsMessage = getSMS(propertiesManager.getAdvovatePaymentLegalClerkApprovalSms(),
									advocatePaymentMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}
					}

					if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

						if (legalAdvisor) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getAdvovatePaymentLegalAdvisorEmailSubject(),
									propertiesManager.getAdvovatePaymentLegalAdvisorEmailBody(), advocatePaymentMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}

						if (approvalLegalClerk) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getAdvovatePaymentLegalClerkApprovalEmailSubject(),
									propertiesManager.getAdvovatePaymentLegalClerkApprovalEmailBody(),
									advocatePaymentMessage, userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}
					}
				}

			} else if (advocatePayment.getModeOfPayment() != null) {

				advocate = Boolean.TRUE;

				for (UserDetail userDetail : userDetails) {

					if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

						if (advocate) {

							SmsMessage smsMessage = getSMS(propertiesManager.getAdvovatePaymentAdvocateSms(),
									advocatePaymentMessage, userDetail.getMobileNumber());
							kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
						}
					}

					if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

						if (advocate) {

							EmailMessage emailMessage = getEmail(
									propertiesManager.getAdvovatePaymentAdvocateEmailSubject(),
									propertiesManager.getAdvovatePaymentAdvocateEmailBody(), advocatePaymentMessage,
									userDetail.getEmailId());
							kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
						}
					}
				}
			}
		}
	}

	public void sendEmailAndSmsForOpinion(OpinionRequest opinionRequest) throws Exception {

		advocate = Boolean.TRUE;
		approvingAuthority = Boolean.TRUE;
		legalClerk = Boolean.TRUE;

		Map<Object, Object> opinionMessage = new HashMap<Object, Object>();
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(propertiesManager.getRolesCode());
		List<UserDetail> userDetails = null;

		for (Opinion opinion : opinionRequest.getOpinions()) {

			String departmentName = mdmsRepository.getDepartmentName(opinion.getTenantId(),
					opinion.getDepartmentName().getCode(), opinionRequest.getRequestInfo());
			opinionMessage.put("Opinion No", opinion.getOpinionOn());
			opinionMessage.put("Department Name", departmentName);
			opinionMessage.put("Dated", TimeStampUtil.getDateWithoutTimezone(opinion.getOpinionRequestDate()));
			opinionMessage.put("ULB Name", opinion.getTenantId());
			userDetails = userReository.getUser(opinion.getTenantId(), roleCodes, opinionRequest.getRequestInfo());

			for (UserDetail userDetail : userDetails) {

				opinionMessage.put("Advocate", userDetail.getName());
				opinionMessage.put("Approving Authority", userDetail.getName());
				opinionMessage.put("Legal clerk", userDetail.getName());

				if (userDetail.getMobileNumber() != null && userDetail.getMobileNumber() != "") {

					if (advocate) {

						SmsMessage smsMessage = getSMS(propertiesManager.getOpinionAdvocateSms(), opinionMessage,
								userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}

					if (approvingAuthority) {

						SmsMessage smsMessage = getSMS(propertiesManager.getOpinionApproveAuthoritySms(),
								opinionMessage, userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}

					if (legalClerk) {

						SmsMessage smsMessage = getSMS(propertiesManager.getOpinionLegalClerkSms(), opinionMessage,
								userDetail.getMobileNumber());
						kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
					}
				}
				if (userDetail.getEmailId() != null && userDetail.getEmailId() != "") {

					if (advocate) {

						EmailMessage emailMessage = getEmail(propertiesManager.getOpinionAdvocateEmailSubject(),
								propertiesManager.getOpinionAdvocateEmailBody(), opinionMessage,
								userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}

					if (approvingAuthority) {

						EmailMessage emailMessage = getEmail(propertiesManager.getOpinionApproveAuthorityEmailSubject(),
								propertiesManager.getOpinionApproveAuthorityEmailBody(), opinionMessage,
								userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}

					if (legalClerk) {

						EmailMessage emailMessage = getEmail(propertiesManager.getOpinionLegalClerkEmailSubject(),
								propertiesManager.getOpinionLegalClerkEmailBody(), opinionMessage,
								userDetail.getEmailId());
						kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
					}
				}
			}
		}
	}

	private SmsMessage getSMS(String smsTemplate, Map<Object, Object> mappingParams, String mobileNumber) {

		String message = notificationUtil.buildSmsMessage(smsTemplate, mappingParams);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		return smsMessage;
	}

	private EmailMessage getEmail(String emailSubjectTemplate, String emailBodyTemplate,
			Map<Object, Object> mappingParams, String emailId) {

		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setSubjectTemplateName(emailSubjectTemplate);
		emailMessageContext.setSubjectTemplateValues(mappingParams);
		emailMessageContext.setBodyTemplateName(emailBodyTemplate);
		emailMessageContext.setBodyTemplateValues(mappingParams);
		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);
		return emailMessage;
	}
}