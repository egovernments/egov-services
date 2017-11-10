package org.egov.lams.notification.service;

import java.text.MessageFormat;

import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.Agreement;
import org.egov.lams.notification.model.Allottee;
import org.egov.lams.notification.model.Asset;
import org.egov.lams.notification.web.contract.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	private PropertiesManager propertiesManager;

	public String getCreateInitiateMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		Double totalAmount = agreement.getSecurityDeposit() + agreement.getGoodWillAmount();
		String message = MessageFormat.format(propertiesManager.getCreateNotificationMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAcknowledgementNumber(),
				agreement.getRent(), agreement.getSecurityDeposit(), agreement.getGoodWillAmount(), totalAmount,
				tenant.getName());
		return message;
	}

	public String getCreateApprovalMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getCreateApproveMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), agreement.getRent(),
				tenant.getName());
		return message;
	}

	public String getCreateRejectedMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getCreateRejectMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAcknowledgementNumber(), tenant.getName());
		return message;
	}

	public String getEvictInitiateMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {

		String message = MessageFormat.format(propertiesManager.getEvictNotificationMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), tenant.getName());
		return message;
	}

	public String getEvictApprovalMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getEvictApproveMessage(), allottee.getName(),
				agreement.getAgreementNumber(), asset.getCategory().getName(), asset.getName(), tenant.getName());
		return message;
	}

	public String getEvictRejectedMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getEvictRejectMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAcknowledgementNumber(), tenant.getName());
		return message;
	}
	
	public String getCancelInitiateMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {

		String message = MessageFormat.format(propertiesManager.getCancelNotificationMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), tenant.getName());
		return message;
	}

	public String getCancelApprovalMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getCancelApproveMessage(), allottee.getName(),
				agreement.getAgreementNumber(), asset.getCategory().getName(), asset.getName(), tenant.getName());
		return message;
	}

	public String getCancelRejectedMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getCancelRejectMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), tenant.getName());
		return message;
	}
	
	/*
	 * email and sms messages for objection
	 */
	public String getObjectionInitiateMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getObjectionInitiateMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), agreement.getRent(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}

	public String getObjectionApproveMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getObjectionApproveMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}

	public String getObjectionRejectMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getObjectionRejectMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), agreement.getRent(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}
    
    /*
     * sms and email notification for judgement
     */
	public String getJudgementnInitiateMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getJudgementInitiateMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), agreement.getRent(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}

	public String getJudgementApproveMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getJudgementApproveMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}

	public String getJudgementRejectMessage(Agreement agreement, Asset asset, Allottee allottee, Tenant tenant) {
		String message = MessageFormat.format(propertiesManager.getJudgementRejectMessage(), allottee.getName(),
				asset.getCategory().getName(), asset.getName(), agreement.getAgreementNumber(), agreement.getRent(),
				agreement.getObjection().getCourtFixedRent(), tenant.getName());
		return message;
	}
    /*
     * subects for different workflows
     */
	public String getCreateSubject(Agreement agreement) {
		String message = MessageFormat.format(propertiesManager.getCreateSubject(), agreement.getAcknowledgementNumber());
		return message;
	}
	
	public String getEvictSubject(Agreement agreement) {
		String message = MessageFormat.format(propertiesManager.getEvictSubject(), agreement.getAgreementNumber());
		return message;
	}
	
	public String getCancelSubject(Agreement agreement) {
		String message = MessageFormat.format(propertiesManager.getCancelSubject(), agreement.getAgreementNumber());
		return message;
	}
    
	public String getObjectionSubject(Agreement agreement) {
		String message = MessageFormat.format(propertiesManager.getObjectionSubject(), agreement.getAgreementNumber());
		return message;
	}

	public String getJudgementSubject(Agreement agreement) {
		String message = MessageFormat.format(propertiesManager.getJudgementSubject(), agreement.getAgreementNumber());
		return message;
	}

}
