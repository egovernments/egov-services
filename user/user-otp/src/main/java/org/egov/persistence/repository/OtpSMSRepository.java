package org.egov.persistence.repository;

import org.egov.domain.model.OtpRequest;
import org.egov.domain.service.LocalizationService;
import org.egov.persistence.contract.SMSRequest;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

import java.util.Map;

@Service
@Slf4j
public class OtpSMSRepository {
	private static final String LOCALIZATION_KEY_REGISTER_SMS = "sms.register.otp.msg";
	private static final String LOCALIZATION_KEY_LOGIN_SMS = "sms.login.otp.msg";
	private static final String LOCALIZATION_KEY_PWD_RESET_SMS = "sms.pwd.reset.otp.msg";
	
	private CustomKafkaTemplate<String, SMSRequest> kafkaTemplate;
	private String smsTopic;
	
	@Autowired
	private LocalizationService localizationService;

	@Autowired
	public OtpSMSRepository(CustomKafkaTemplate<String, SMSRequest> kafkaTemplate,
			@Value("${sms.topic}") String smsTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.smsTopic = smsTopic;
	}

	public void send(OtpRequest otpRequest, String otpNumber) {
		final String message = getMessage(otpNumber, otpRequest);
		kafkaTemplate.send(smsTopic, new SMSRequest(otpRequest.getMobileNumber(), message));
	}

	private String getMessage(String otpNumber, OtpRequest otpRequest) {
		final String messageFormat = getMessageFormat(otpRequest);
		return format(messageFormat, otpNumber);
	}

	private String getMessageFormat(OtpRequest otpRequest) {
		Map<String, String> localisedMsgs = localizationService.getLocalisedMessages(otpRequest.getTenantId(), "en_IN", "egov-user");
		if(localisedMsgs.isEmpty()) {
			log.info("Localization Service didn't return any msgs so using default...");
			localisedMsgs.put(LOCALIZATION_KEY_REGISTER_SMS, "Dear Citizen, Your OTP to complete your mSeva Registration is %s.");
			localisedMsgs.put(LOCALIZATION_KEY_LOGIN_SMS, "Dear Citizen, Your Login OTP is %s.");
			localisedMsgs.put(LOCALIZATION_KEY_PWD_RESET_SMS, "Dear Citizen, Your OTP for recovering password is %s.");
		}
		String message = null;
		
		if (otpRequest.isRegistrationRequestType())
			message = localisedMsgs.get(LOCALIZATION_KEY_REGISTER_SMS);
		else if (otpRequest.isLoginRequestType()) 
			message = localisedMsgs.get(LOCALIZATION_KEY_LOGIN_SMS);
		else
			message = localisedMsgs.get(LOCALIZATION_KEY_PWD_RESET_SMS);
		
		return message;
	}
}
