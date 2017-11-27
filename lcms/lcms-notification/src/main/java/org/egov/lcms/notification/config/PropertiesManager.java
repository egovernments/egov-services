package org.egov.lcms.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {
	
	@Value("${app.timezone}")
	private String appTimeZone;
	
	@Value("${roles.code}")
	private String rolesCode;
	
	@Value("${lcms-notification.template.type}")
	private String templateType;
	
	@Value("${lcms-notification.template.folder}")
	private String templateFolder;

	@Value("${lcms-notification.template.priority}")
	private String templatePriority;
	
	/**
	 * Producer Topic names for sending Email and SMS
	 */
	@Value("${egov.lcms.lcms-notification.email}")
	private String emailNotification;
	
	@Value("${egov.lcms.lcms-notification.sms}")
	private String smsNotification;
	
	/**
	 * Consumer Topic Names for:
	 * 1.Summon, 2.Assigning Advocate, 3.Case Registration, 
	 * 4.Vakalatnama Generation, 5Para wise comments, 
	 * 6.Hearing Process Details Entry, 7.Advocate Payment, Opinion
	 */

	@Value("${egov.lcms.lcms-notification.summon.entry}")
    private String summonTopic;
	
	@Value("${egov.lcms.lcms-notification.assign.advocate}")
	private String assigningAdvocateTopic;
	
	@Value("${egov.lcms.lcms-notification.case.registration}")
	private String caseRegistrationTopic;
	
	@Value("${egov.lcms.lcms-notification.vakalatnama.generation}")
	private String vakalatnamaGenerationTopic;
	
	@Value("${egov.lcms.lcms-notification.parawise.comments}")
	private String parawiseCommentsTopic;
	
	@Value("${egov.lcms.lcms-notification.hearing.processdetails}")
	private String hearingProcessdetailsTopic;
	
	@Value("${egov.lcms.lcms-notification.advocate.payment}")
	private String advocatePaymentTopic;
	
	@Value("${egov.lcms.lcms-notification.update.advocate.payment}")
	private String updateAdvocatePaymentTopic;
	
	@Value("${egov.lcms.lcms-notification.opinion}")
	private String opinionTopic;
	
	/**
	 * Summon Email and SMS template
	 * 
	 * Assign Authority Email and SMS
	 */
	@Value("${summon.sms}")
	private String summonSms;
	
	@Value("${summon.email.subject}")
	private String summonEmailSubject;
	
	@Value("${summon.email.body}")
	private String summonEmailBody;
	
	/**
	 * Assigning Advocate Email and SMS template
	 * 
	 * Legal Clerk Email and SMS
	 */
	@Value("${assign.advocate.legal.clerk.sms}")
	private String assignAdvocateLegalClerkSms;
	
	@Value("${assign.advocate.legal.clerk.email.subject}")
	private String assignAdvocateLegalClerkEmailSubject;
	
	@Value("${assign.advocate.legal.clerk.email.body}")
	private String assignAdvocateLegalClerkEmailBody;
	
	/**
	 * Case Registraion Authority
	 */
	@Value("${assign.advocate.case.registration.authority.sms}")
	private String assignAdvocateRegAuthSms;
	
	@Value("${assign.advocate.case.registration.authority.email.subject}")
	private String assignAdvocateRegAuthEmailSubject;
	
	@Value("${assign.advocate.case.registration.authority.email.body}")
	private String assignAdvocateRegAuthEmailBody;
	
	/**
	 * Case Registraion Email and SMS 
	 * 
	 * Legal clerk Email and SMS
	 */
	@Value("${case.registration.legal.clerk.sms}")
	private String caseRegLegalClerkSms;
	
	@Value("${case.registration.legal.clerk.email.subject}")
	private String caseRegLegalClerkEmailSubject;
	
	@Value("${case.registration.legal.clerk.email.boy}")
	private String caseRegLegalClerkEmailBody;
	
	/**
	 * Vakalatnama Generation Email and SMS
	 * 
	 * Advocate Email and SMS
	 */
	@Value("${vakalatnama.generation.advocate.sms}")
	private String vakalatnamaAdvocateSms;
	
	@Value("${vakalatnama.generation.advocate.email.subject}")
	private String vakalatnamaAdvocateEmailSubject;
	
	@Value("${vakalatnama.generation.advocate.email.body}")
	private String vakalatnamaAdvocateEmailBody;
	
	/**
	 * Concerned Department Email and SMS
	 */
	@Value("${vakalatnama.generation.advocate.concerned.dept.sms}")
	private String vakalatnamaConcernedDeptSms;
	
	@Value("${vakalatnama.generation.advocate.concerned.dept.email.subject}")
	private String vakalatnamaConcernedDeptEmailSubject;
	
	@Value("${vakalatnama.generation.advocate.concerned.dept.email.body}")
	private String vakalatnamaConcernedDeptEmailBody;
	
	/**
	 * Para wise Comments Email and SMS
	 * 
	 * Legal clerk Email and SMS
	 */
	@Value("${parawise.comments.legal.clerk.sms}")
	private String parawiseCommentLegalClerkSms;
	
	@Value("${parawise.comments.legal.clerk.email.subject}")
	private String parawiseCommentLegalClerkEmailSubject;
	
	@Value("${parawise.comments.legal.clerk.email.body}")
	private String parawiseCommentLegalClerkEmailBody;
	
	/**
	 * Hearing Process Details Email and SMS
	 * 
	 * Legal/Concerned Department Email and SMS
	 */
	@Value("${hearing.processdetails.sms}")
	private String hearingProcessSms;
	
	@Value("${hearing.nexthearing.processdetails.sms}")
	private String nextHearingProcessSMS;
	
	@Value("${hearing.processdetails.email.subject}")
	private String hearingProcessEmailSubject;
	
	@Value("${hearing.processdetails.email.body}")
	private String hearingProcessEmailBody;
	
	@Value("{hearing.nexthearing.processdetails.email.body}")
	private String nextHearingProcessEmailBody;
	
	/**
	 * Advocate Payment Email and SMS
	 * 
	 * Legal clerk Email and SMS
	 */
	@Value("${advocate.payment.legal.clerk.sms}")
	private String advovatePaymentLegalClerkSms;
	
	@Value("${advocate.payment.legal.clerk.email.subject}")
	private String advovatePaymentLegalClerkEmailSubject;
	
	@Value("${advocate.payment.legal.clerk.email.body}")
	private String advovatePaymentLegalClerkEmailBody;
	
	/**
	 * Sanctioning Authority Email and SMS
	 */
	@Value("${advocate.payment.sanctioning.authority.sms}")
	private String advovatePaymentSanctioningAuthSms;
	
	@Value("${advocate.payment.sanctioning.authority.email.subject}")
	private String advovatePaymentSanctioningAuthEmailSubject;
	
	@Value("${advocate.payment.sanctioning.authority.email.body}")
	private String advovatePaymentSanctioningAuthEmailBody;
	
	/**
	 * Legal Advisor Email and SMS
	 */
	@Value("${advocate.payment.legal.advisor.sms}")
	private String advovatePaymentLegalAdvisorSms;
	
	@Value("${advocate.payment.legal.advisor.email.subject}")
	private String advovatePaymentLegalAdvisorEmailSubject;
	
	@Value("${advocate.payment.legal.advisor.email.body}")
	private String advovatePaymentLegalAdvisorEmailBody;
	
	/**
	 * Legal Email and SMS for Approval
	 */
	@Value("${advocate.payment.legal.clerk.approval.sms}")
	private String advovatePaymentLegalClerkApprovalSms;
	
	@Value("${advocate.payment.legal.clerk.approval.email.subject}")
	private String advovatePaymentLegalClerkApprovalEmailSubject;
	
	@Value("${advocate.payment.legal.clerk.approval.email.body}")
	private String advovatePaymentLegalClerkApprovalEmailBody;
	
	/**
	 * Advocate Email and SMS
	 */
	@Value("${advocate.payment.advocate.sms}")
	private String advovatePaymentAdvocateSms;
	
	@Value("${advocate.payment.advocate.email.subject}")
	private String advovatePaymentAdvocateEmailSubject;
	
	@Value("${advocate.payment.advocate.email.body}")
	private String advovatePaymentAdvocateEmailBody;
	
	/**
	 * Opinion Email and SMS
	 * 
	 * Legal clerk Email and SMS
	 */
	@Value("${opinion.legal.clerk.sms}")
	private String opinionLegalClerkSms;
	
	@Value("${opinion.legal.clerk.email.subject}")
	private String opinionLegalClerkEmailSubject;
	
	@Value("${opinion.legal.clerk.email.body}")
	private String opinionLegalClerkEmailBody;
	
	/**
	 * Approve Authority Email and SMS
	 */
	@Value("${opinion.approve.authority.sms}")
	private String opinionApproveAuthoritySms;
	
	@Value("${opinion.approve.authority.email.subject}")
	private String opinionApproveAuthorityEmailSubject;
	
	@Value("${opinion.approve.authority.email.body}")
	private String opinionApproveAuthorityEmailBody;
	
	/**
	 * Advocate Email and SMS
	 */
	@Value("${opinion.advocate.sms}")
	private String opinionAdvocateSms;
	
	@Value("${opinion.advocate.email.subject}")
	private String opinionAdvocateEmailSubject;
	
	@Value("${opinion.advocate.email.body}")
	private String opinionAdvocateEmailBody;
	
	/**
	 * User search url info
	 */
	@Value("${egov.services.egov_user.hostname}")
	private String userHostName;
	
	@Value("${egov.services.egov_user.basepath}")
	private String userBasePath;
	
	@Value("${egov.services.egov_user.searchpath}")
	private String userSearchPath;
	
	/**
	 * Host and base path name
	 */
	@Value("${egov.services.lcms-services.hostname}")
	private String hostName;
	
	@Value("${basepath}")
	private String basepath;
	/**
	 * Advocate search url info
	 */	
	@Value("${advocate.searchpath}")
	private String advocateSearchpath;
	
	/**
	 * Case search url info
	 */	
	@Value("${case.searchpath}")
	private String caseSearchpath;
	
	/**
	 * mdms service url info
	 */
	@Value("${egov.services.mdms-services.hostname}")
	private String mdmsBasePath;
	
	@Value("${egov.services.mdms.searchpath}")
	private String mdmsSearhPath;
	
	@Value("${egov.common.master.module.name}")
	private String commonModuleName;
}
