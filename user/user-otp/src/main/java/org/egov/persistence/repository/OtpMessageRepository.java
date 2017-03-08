package org.egov.persistence.repository;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.contract.SMSRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.lang.String.format;

@Service
public class OtpMessageRepository {
    private static final String SMS_OTP_MESSAGE = "Use OTP {0} for portal registration.";
    private static final String OTP_SEND_FAILURE_MESSAGE = "Sending of User OTP message failed";
    private KafkaTemplate<String, SMSRequest> kafkaTemplate;
    private String smsTopic;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OtpMessageRepository(KafkaTemplate<String, SMSRequest> kafkaTemplate,
                                @Value("sms.topic") String smsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.smsTopic = smsTopic;
    }

    public void send(OtpRequest otpRequest, String otpNumber) {
        try {
            kafkaTemplate.send(smsTopic, new SMSRequest(otpRequest.getMobileNumber(), getMessage(otpNumber))).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(OTP_SEND_FAILURE_MESSAGE, e);
            throw new RuntimeException(e);
        }
    }

    private String getMessage(String otpNumber) {
        return format(SMS_OTP_MESSAGE, otpNumber);
    }
}
