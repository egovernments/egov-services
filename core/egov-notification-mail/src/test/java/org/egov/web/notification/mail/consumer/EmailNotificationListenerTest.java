package org.egov.web.notification.mail.consumer;

import org.egov.web.notification.mail.consumer.contract.EmailRequest;
import org.egov.web.notification.mail.model.Email;
import org.egov.web.notification.mail.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailNotificationListenerTest {

    @Mock
    EmailService emailService;

    @Test
    public void test_should_send_sms() throws Exception {
        EmailNotificationListener emailNotificationListener = new EmailNotificationListener(emailService);
        EmailRequest emailRequest = new EmailRequest("email", "subject", "body", "sender");

        emailNotificationListener.listen(emailRequest);

        verify(emailService).sendEmail(new Email("email", "subject", "body"));
    }
}