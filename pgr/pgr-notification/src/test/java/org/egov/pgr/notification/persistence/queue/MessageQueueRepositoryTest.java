package org.egov.pgr.notification.persistence.queue;

import org.egov.pgr.notification.domain.model.EmailRequest;
import org.egov.pgr.notification.domain.model.SMSRequest;
import org.egov.pgr.notification.persistence.queue.contract.EmailMessage;
import org.egov.pgr.notification.persistence.queue.contract.SmsMessage;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.support.SendResult;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageQueueRepositoryTest {

    private static final String SMS_TOPIC = "smsTopic";
    private static final String EMAIL_TOPIC = "emailTopic";
    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    private MessageQueueRepository messageQueueRepository;

    @Before
    public void before() {
        messageQueueRepository = new MessageQueueRepository(kafkaTemplate, SMS_TOPIC, EMAIL_TOPIC);
    }

    @Test
    public void test_should_send_sms_message_to_sms_topic() {
        final String mobileNumber = "mobileNumber";
        final String smsMessage = "smsMessage";
        final SmsMessage expectedSMSMessage = new SmsMessage(mobileNumber, smsMessage);
        final SendResult<String, Object> sendResult = new SendResult<>(null, null);
        when(kafkaTemplate.send(SMS_TOPIC, expectedSMSMessage)).thenReturn(sendResult);

        messageQueueRepository.sendSMS(new SMSRequest(smsMessage, mobileNumber));

        verify(kafkaTemplate).send(SMS_TOPIC, expectedSMSMessage);
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_runtime_exception_when_send_sms_message_fails() {
        final String mobileNumber = "mobileNumber";
        final String smsMessage = "smsMessage";
        final SmsMessage expectedSMSMessage = new SmsMessage(mobileNumber, smsMessage);
        when(kafkaTemplate.send(SMS_TOPIC, expectedSMSMessage)).thenThrow(new RuntimeException());

        messageQueueRepository.sendSMS(new SMSRequest(smsMessage, mobileNumber));
    }

    @Test
    public void test_should_send_email_to_email_topic() {
        final String emailAddress = "email@email.com";
        final SendResult<String, Object> sendResult = new SendResult<>(null, null);
        final String body = "body";
        final String subject = "subject";
        final EmailMessage expectedEmailMessage = EmailMessage.builder()
            .email(emailAddress)
            .subject(subject)
            .body(body)
            .sender("")
            .build();
        when(kafkaTemplate.send(EMAIL_TOPIC, expectedEmailMessage)).thenReturn(sendResult);
        final EmailRequest emailRequest = new EmailRequest(subject, body, emailAddress);

        messageQueueRepository.sendEmail(emailRequest);

        verify(kafkaTemplate).send(EMAIL_TOPIC, expectedEmailMessage);
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_runtime_exception_when_send_email_message_fails() {
        final String emailAddress = "email@email.com";
        final String body = "body";
        final String subject = "subject";
        final EmailMessage expectedEmailMessage = EmailMessage.builder()
            .email(emailAddress)
            .subject(subject)
            .body(body)
            .sender("")
            .build();

        when(kafkaTemplate.send(EMAIL_TOPIC, expectedEmailMessage)).thenThrow(new RuntimeException());
        final EmailRequest emailRequest = new EmailRequest(subject, body, emailAddress);

        messageQueueRepository.sendEmail(emailRequest);
    }

}