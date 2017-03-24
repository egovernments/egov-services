package org.egov.persistence.queue;

import org.egov.domain.model.EmailRequest;
import org.egov.persistence.queue.contract.EmailMessage;
import org.egov.persistence.queue.contract.SmsMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageQueueRepositoryTest {

    private static final String SMS_TOPIC = "smsTopic";
    private static final String EMAIL_TOPIC = "emailTopic";
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
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
        final SettableListenableFuture<SendResult<String, Object>> listenableFuture =
            new SettableListenableFuture<>();
        listenableFuture.set(new SendResult<>(null, null));
        when(kafkaTemplate.send(SMS_TOPIC, expectedSMSMessage)).thenReturn(listenableFuture);

        messageQueueRepository.sendSMS(mobileNumber, smsMessage);

        verify(kafkaTemplate).send(SMS_TOPIC, expectedSMSMessage);
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_runtime_exception_when_send_sms_message_fails() {
        final String mobileNumber = "mobileNumber";
        final String smsMessage = "smsMessage";
        final SmsMessage expectedSMSMessage = new SmsMessage(mobileNumber, smsMessage);
        final SettableListenableFuture<SendResult<String, Object>> listenableFuture =
            new SettableListenableFuture<>();
        listenableFuture.setException(new InterruptedException());
        when(kafkaTemplate.send(SMS_TOPIC, expectedSMSMessage)).thenReturn(listenableFuture);

        messageQueueRepository.sendSMS(mobileNumber, smsMessage);
    }

    @Test
    public void test_should_send_email_to_email_topic() {
        final String emailAddress = "email@email.com";
        final SettableListenableFuture<SendResult<String, Object>> listenableFuture =
            new SettableListenableFuture<>();
        listenableFuture.set(new SendResult<>(null, null));
        final String body = "body";
        final String subject = "subject";
        final EmailMessage expectedEmailMessage = EmailMessage.builder()
            .email(emailAddress)
            .subject(subject)
            .body(body)
            .sender("")
            .build();
        when(kafkaTemplate.send(EMAIL_TOPIC, expectedEmailMessage)).thenReturn(listenableFuture);

        messageQueueRepository.sendEmail(emailAddress, new EmailRequest(subject, body));

        verify(kafkaTemplate).send(EMAIL_TOPIC, expectedEmailMessage);
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_runtime_exception_when_send_email_message_fails() {
        final String emailAddress = "email@email.com";
        final SettableListenableFuture<SendResult<String, Object>> listenableFuture =
            new SettableListenableFuture<>();
        listenableFuture.setException(new InterruptedException());
        final String body = "body";
        final String subject = "subject";
        final EmailMessage expectedEmailMessage = EmailMessage.builder()
            .email(emailAddress)
            .subject(subject)
            .body(body)
            .sender("")
            .build();

        when(kafkaTemplate.send(EMAIL_TOPIC, expectedEmailMessage)).thenReturn(listenableFuture);

        messageQueueRepository.sendEmail(emailAddress, new EmailRequest(subject, body));
    }

}