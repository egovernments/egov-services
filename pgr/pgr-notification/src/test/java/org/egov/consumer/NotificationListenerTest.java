package org.egov.consumer;

import org.egov.domain.model.SevaRequest;
import org.egov.domain.service.EmailService;
import org.egov.domain.service.SMSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotificationListenerTest {

    @Mock
    private SMSService smsService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    public void test_should_send_sms() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();

        notificationListener.process(sevaRequestMap);

        verify(smsService).send(any(SevaRequest.class));
    }

    @Test
    public void test_should_send_email() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();

        notificationListener.process(sevaRequestMap);

        verify(emailService).send(any(SevaRequest.class));
    }
}