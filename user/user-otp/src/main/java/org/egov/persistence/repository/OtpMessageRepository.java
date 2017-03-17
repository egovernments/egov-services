package org.egov.persistence.repository;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.LogAwareKafkaTemplate;
import org.egov.persistence.contract.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class OtpMessageRepository {
    private static final String SMS_OTP_MESSAGE = "Use OTP %s for portal registration.";
    private LogAwareKafkaTemplate<String, SMSRequest> kafkaTemplate;
    private String smsTopic;

    @Autowired
    public OtpMessageRepository(LogAwareKafkaTemplate<String, SMSRequest> kafkaTemplate,
                                @Value("${sms.topic}") String smsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.smsTopic = smsTopic;
    }

    public void send(OtpRequest otpRequest, String otpNumber) {
            kafkaTemplate.send(smsTopic, new SMSRequest(otpRequest.getMobileNumber(), getMessage(otpNumber)));
    }

    private String getMessage(String otpNumber) {
        return format(SMS_OTP_MESSAGE, otpNumber);
    }
}
