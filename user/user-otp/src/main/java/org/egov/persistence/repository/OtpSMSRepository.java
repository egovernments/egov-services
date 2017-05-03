package org.egov.persistence.repository;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.LogAwareKafkaTemplate;
import org.egov.persistence.contract.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class OtpSMSRepository {
    private static final String SMS_REGISTER_OTP_MESSAGE = "Use OTP %s for portal registration.";
    private static final String SMS_PASSWORD_RESET_OTP_MESSAGE = "Your OTP for recovering password is %s.";
    private LogAwareKafkaTemplate<String, SMSRequest> kafkaTemplate;
    private String smsTopic;

    @Autowired
    public OtpSMSRepository(LogAwareKafkaTemplate<String, SMSRequest> kafkaTemplate,
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
		return otpRequest.isRegistrationRequestType()
				? SMS_REGISTER_OTP_MESSAGE
				: SMS_PASSWORD_RESET_OTP_MESSAGE;
	}
}
