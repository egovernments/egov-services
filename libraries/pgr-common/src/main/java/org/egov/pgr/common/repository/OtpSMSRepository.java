package org.egov.pgr.common.repository;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class OtpSMSRepository {
    private static final String SMS_REGISTER_OTP_MESSAGE =
        "Dear Citizen, to register the complaint, the OTP is %s.\nPowered by www.egovernments.org";
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    private String smsTopic;

    @Autowired
    public OtpSMSRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${sms.topic}") String smsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.smsTopic = smsTopic;
    }

    public void send(org.egov.pgr.common.model.OtpRequest otpRequest, String otpNumber) {
		final String message = getMessage(otpNumber);
		kafkaTemplate.send(smsTopic, new SMSRequest(otpRequest.getMobileNumber(), message));
    }

    private String getMessage(String otpNumber) {
		return format(SMS_REGISTER_OTP_MESSAGE, otpNumber);
    }

}
