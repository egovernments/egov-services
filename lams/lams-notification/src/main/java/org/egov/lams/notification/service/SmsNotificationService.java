package org.egov.lams.notification.service;

import java.text.MessageFormat;

import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.models.Agreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService {

	@Autowired
	private PropertiesManager propertiesManager;

	public String getSmsMessage(Agreement agreement) {

		Double totalAmount = agreement.getSecurityDeposit() + agreement.getBankGuaranteeAmount();
		String message = MessageFormat.format(propertiesManager.getNotificationMessage(),
				agreement.getAllottee().getName(), agreement.getAsset().getCategory(), agreement.getAsset().getName(),
				agreement.getAcknowledgementNumber(), agreement.getRent(), agreement.getSecurityDeposit(),
				agreement.getBankGuaranteeAmount(), totalAmount,
				agreement.getAsset().getLocationDetails().getRevenueWard());

		return message;

	}

	public String getApprovalMessage(Agreement agreement) {

		String message = MessageFormat.format(propertiesManager.getApproveMessage(), agreement.getAllottee().getName(),
				agreement.getAsset().getCategory(), agreement.getAsset().getName(),
				agreement.getAcknowledgementNumber(), agreement.getRent(),
				agreement.getAsset().getLocationDetails().getRevenueWard());

		return message;
	}

	public String getRejectedMessage(Agreement agreement) {

		String message = MessageFormat.format(propertiesManager.getRejectMessage(), agreement.getAllottee().getName(),
				agreement.getAsset().getCategory(), agreement.getAsset().getName(),
				agreement.getAcknowledgementNumber(), agreement.getAsset().getLocationDetails().getRevenueWard());

		return message;
	}
}
