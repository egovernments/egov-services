package org.egov.pgr.notification.persistence.queue;

import org.egov.pgr.notification.domain.model.EmailRequest;
import org.egov.pgr.notification.domain.model.SMSRequest;
import org.egov.pgr.notification.persistence.queue.contract.EmailMessage;
import org.egov.pgr.notification.persistence.queue.contract.SmsMessage;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
public class MessageQueueRepository {
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    private String smsTopic;
    private String emailTopic;

    public MessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                  @Value("${sms.topic}") String smsTopic,
                                  @Value("${email.topic}") String emailTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.smsTopic = smsTopic;
        this.emailTopic = emailTopic;
    }

    public void sendSMS(SMSRequest smsRequest) {
        final SmsMessage smsMessage = new SmsMessage(smsRequest.getMobileNumber(), smsRequest.getMessage());
        sendMessage(smsTopic, smsMessage);
    }

    public void sendEmail(EmailRequest emailRequest) {
        final EmailMessage emailMessage = EmailMessage.builder()
            .body(emailRequest.getBody())
            .subject(emailRequest.getSubject())
            .sender(EMPTY)
            .email(emailRequest.getEmail())
            .build();
        sendMessage(emailTopic, emailMessage);
    }

    private void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

}
