package org.egov.web.notification.sms.consumer;

import org.egov.web.notification.sms.services.MessagePriority;
import org.egov.web.notification.sms.services.SMSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SmsConsumerTest {

    @Autowired
    private Sink sink;

    @MockBean
    private SMSService smsService;

    @Test
    public void testShouldSendSmsWhenPayloadIsReceived() {
        final String payload = "{\"mobileNumber\":\"123545\", \"message\": \"testmessage\", \"foo\":\"bar\"}";
        Message<String> smsRequest = new GenericMessage<>(payload);

        sink.input().send(smsRequest);

        verify(smsService, times(1))
                .sendSMS("123545", "testmessage", MessagePriority.HIGH);
    }
}