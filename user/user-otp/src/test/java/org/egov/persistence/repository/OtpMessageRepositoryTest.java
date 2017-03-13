package org.egov.persistence.repository;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.contract.SMSRequest;
import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OtpMessageRepositoryTest {
    private static final String SMS_TOPIC = "sms.topic";
    @Mock
    private KafkaTemplate<String, SMSRequest> kafkaTemplate;
    private OtpMessageRepository otpMessageRepository;

    @Before
    public void before() {
        otpMessageRepository = new OtpMessageRepository(kafkaTemplate, SMS_TOPIC);
    }

    @Test
    public void test_should_send_sms_request_to_topic() {
        final String mobileNumber = "mobileNumber";
        final String tenantId = "tenantId";
        final String otpNumber = "otpNumber";
        final OtpRequest otpRequest = new OtpRequest(mobileNumber, tenantId);
        final SettableListenableFuture<SendResult<String, SMSRequest>> response = new SettableListenableFuture<>();
        response.set(new SendResult<>(null, null));
        final String expectedMessage = "Use OTP otpNumber for portal registration.";
        final SMSRequest expectedSmsRequest = new SMSRequest(mobileNumber, expectedMessage);

        when(kafkaTemplate.send(eq(SMS_TOPIC), argThat(new SmsRequestMatcher(expectedSmsRequest)))).thenReturn(response);

        otpMessageRepository.send(otpRequest, otpNumber);

        verify(kafkaTemplate).send(eq(SMS_TOPIC), argThat(new SmsRequestMatcher(expectedSmsRequest)));
    }

    @Test(expected = RuntimeException.class)
    public void test_should_raise_run_time_exception_when_sending_message_to_topic_fails() {
        final String mobileNumber = "mobileNumber";
        final String tenantId = "tenantId";
        final String otpNumber = "otpNumber";
        final OtpRequest otpRequest = new OtpRequest(mobileNumber, tenantId);
        final SettableListenableFuture<SendResult<String, SMSRequest>> response = new SettableListenableFuture<>();
        response.setException(new InterruptedException());
        final String expectedMessage = "Use OTP otpNumber for portal registration.";
        final SMSRequest expectedSmsRequest = new SMSRequest(mobileNumber, expectedMessage);
        when(kafkaTemplate.send(eq(SMS_TOPIC), argThat(new SmsRequestMatcher(expectedSmsRequest))))
                .thenReturn(response);

        otpMessageRepository.send(otpRequest, otpNumber);
    }

    private class SmsRequestMatcher extends CustomMatcher<SMSRequest> {


        private SMSRequest expectedSMSRequest;

        public SmsRequestMatcher(SMSRequest expectedSMSRequest) {
            super("SMSRequest matcher");
            this.expectedSMSRequest = expectedSMSRequest;
        }

        @Override
        public boolean matches(Object o) {
            SMSRequest actual = (SMSRequest)o;
            return expectedSMSRequest.getMessage().equals(actual.getMessage())
                    && expectedSMSRequest.getMobileNumber().equals(actual.getMobileNumber());
        }
    }
}