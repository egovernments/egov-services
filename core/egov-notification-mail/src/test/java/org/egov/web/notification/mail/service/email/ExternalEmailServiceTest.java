package org.egov.web.notification.mail.service.email;

import org.egov.web.notification.mail.model.Email;
import org.egov.web.notification.mail.service.ExternalEmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExternalEmailServiceTest {

    @Mock
    JavaMailSenderImpl javaMailSender;

    @Test
    public void test_email_service_uses_mail_sender_to_send_email() {
        final String EMAIL_ADDRESS = "email@gmail.com";
        final String SUBJECT = "Subject";
        final String BODY = "body of the email";
        final Email email = new Email(EMAIL_ADDRESS, SUBJECT, BODY);

        ExternalEmailService externalEmailService = new ExternalEmailService(javaMailSender);
        SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setTo(EMAIL_ADDRESS);
        expectedMailMessage.setSubject(SUBJECT);
        expectedMailMessage.setText(BODY);

        externalEmailService.sendEmail(email);

        verify(javaMailSender).send(expectedMailMessage);
    }
}